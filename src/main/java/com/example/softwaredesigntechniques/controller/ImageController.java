package com.example.softwaredesigntechniques.controller;

import com.example.softwaredesigntechniques.domain.image.Image;
import com.example.softwaredesigntechniques.dto.event.EventDto;
import com.example.softwaredesigntechniques.dto.image.ImageDto;
import com.example.softwaredesigntechniques.endpoint.event.EventEndpoint;
import com.example.softwaredesigntechniques.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;
    private final EventEndpoint eventEndpoint;

    @PostMapping("/upload")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> uploadImage(@RequestBody Map<String, String> requestData) {
        try {
            String fileName = requestData.get("fileName");
            String imageData = requestData.get("imageData");
            String contentType = requestData.get("contentType");
            
            log.info("Image upload request received. Filename: {}, Content Type: {}, Base64 Length: {}", 
                    fileName, contentType, imageData != null ? imageData.length() : 0);
            
            if (imageData == null || imageData.isEmpty()) {
                log.warn("Upload failed: Empty image data");
                return ResponseEntity.badRequest().build();
            }
            
            try {
                // Generate a unique file name but keep the original extension
                String fileExtension = "";
                if (fileName != null && fileName.contains(".")) {
                    fileExtension = fileName.substring(fileName.lastIndexOf("."));
                } else if (contentType != null) {
                    // If no extension in filename, try to get it from the content type
                    if (contentType.equals("image/jpeg") || contentType.equals("image/jpg")) {
                        fileExtension = ".jpg";
                    } else if (contentType.equals("image/png")) {
                        fileExtension = ".png";
                    } else if (contentType.equals("image/gif")) {
                        fileExtension = ".gif";
                    }
                }
                
                String uniqueFileName = UUID.randomUUID() + fileExtension;
                
                // Create an Image entity and save it
                Image image = new Image();
                image.setFileName(uniqueFileName);
                image.setImageData(imageData);
                image.setContentType(contentType);
                image.setUploadedAt(LocalDateTime.now());
                
                log.info("Creating Image entity with filename: {}", uniqueFileName);
                log.info("Image data length before save: {}", imageData.length());
                
                // Verify the image data is valid base64
                try {
                    Base64.getDecoder().decode(imageData);
                    log.info("Image data is valid base64");
                } catch (IllegalArgumentException e) {
                    log.error("Invalid base64 data: {}", e.getMessage());
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid base64 data"));
                }
                
                Image savedImage = imageService.saveOrUpdate(image);
                log.info("Image entity saved with ID: {}", savedImage.getId());
                log.info("Image data length after save: {}", savedImage.getImageData() != null ? savedImage.getImageData().length() : 0);
                
                // Return the image ID in a response
                Map<String, Object> response = new HashMap<>();
                response.put("id", savedImage.getId());
                response.put("fileName", savedImage.getFileName());
                response.put("contentType", savedImage.getContentType());
                response.put("uploadedAt", savedImage.getUploadedAt());
                
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                log.error("Error saving image", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body(Map.of("error", "Failed to save image: " + e.getMessage()));
            }
        } catch (Exception e) {
            log.error("Error processing upload request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Failed to process upload request: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> getImage(@PathVariable Long id) {
        try {
            log.info("Image request for ID: {}", id);
            Image image = imageService.get(id);
            log.info("Found image: {}", image.getFileName());
            
            if (image.getImageData() == null || image.getImageData().isEmpty()) {
                log.error("Image data is empty for ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            
            log.info("Image data length before decode: {}", image.getImageData().length());
            
            // Decode the Base64 string to byte array
            byte[] imageData = Base64.getDecoder().decode(image.getImageData());
            log.info("Decoded image data length: {} bytes", imageData.length);
            
            // Determine content type
            String contentType = image.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "image/jpeg"; // Default to JPEG if not specified
            }
            
            log.info("Returning image with content type: {}, size: {} bytes", contentType, imageData.length);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageData);
        } catch (Exception e) {
            log.error("Failed to retrieve image: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve image: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> deleteImage(@PathVariable Long id) {
        try {
            Image image = imageService.get(id);
            
            // Delete the database entry
            imageService.delete(image);
            
            return ResponseEntity.ok(Map.of("message", "Image deleted successfully"));
        } catch (Exception e) {
            log.error("Failed to delete image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete image: " + e.getMessage()));
        }
    }

    @GetMapping("/debug")
    @ResponseBody
    public ResponseEntity<Object> debugImageSystem() {
        try {
            Map<String, Object> debugInfo = new HashMap<>();
            
            // Check database records
            try {
                List<Image> images = imageService.get(List.of());  // Get all images by passing an empty list
                List<Map<String, Object>> imageInfos = new ArrayList<>();
                for (Image image : images) {
                    Map<String, Object> imageInfo = new HashMap<>();
                    imageInfo.put("id", image.getId());
                    imageInfo.put("fileName", image.getFileName());
                    imageInfo.put("contentType", image.getContentType());
                    imageInfo.put("uploadedAt", image.getUploadedAt());
                    imageInfo.put("hasImageData", image.getImageData() != null && !image.getImageData().isEmpty());
                    if (image.getImageData() != null) {
                        imageInfo.put("imageDataLength", image.getImageData().length());
                        // Try to decode the base64 data to verify it's valid
                        try {
                            byte[] decodedData = Base64.getDecoder().decode(image.getImageData());
                            imageInfo.put("decodedDataLength", decodedData.length);
                            imageInfo.put("isValidBase64", true);
                        } catch (IllegalArgumentException e) {
                            imageInfo.put("isValidBase64", false);
                            imageInfo.put("base64Error", e.getMessage());
                        }
                    }
                    
                    // Check if the image has associated event
                    boolean hasEvent = image.getEvent() != null;
                    imageInfo.put("hasEvent", hasEvent);
                    if (hasEvent) {
                        imageInfo.put("eventId", image.getEvent().getId());
                        imageInfo.put("eventTitle", image.getEvent().getTitle());
                    }
                    
                    imageInfos.add(imageInfo);
                }
                debugInfo.put("databaseImages", imageInfos);
                debugInfo.put("imageCount", imageInfos.size());
            } catch (Exception e) {
                debugInfo.put("databaseError", e.getMessage());
                log.error("Error fetching images from database", e);
            }
            
            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            log.error("Error in debug endpoint: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Debug endpoint error: " + e.getMessage()));
        }
    }

    // Add route for default event image
    @GetMapping("/default-event")
    @ResponseBody
    public ResponseEntity<Object> getDefaultEventImage() {
        try {
            // Create a simple colored default image
            int width = 800;
            int height = 400;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            
            // Create a gradient background (blue to light blue)
            GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 102, 204), width, height, new Color(135, 206, 250));
            graphics.setPaint(gradient);
            graphics.fillRect(0, 0, width, height);
            
            // Add some text
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Arial", Font.BOLD, 36));
            String text = "Event";
            FontMetrics metrics = graphics.getFontMetrics();
            int x = (width - metrics.stringWidth(text)) / 2;
            int y = (height - metrics.getHeight()) / 2 + metrics.getAscent();
            graphics.drawString(text, x, y);
            
            graphics.dispose();
            
            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageData = baos.toByteArray();
            
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageData);
        } catch (Exception e) {
            log.error("Failed to generate default image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to generate default image: " + e.getMessage()));
        }
    }

    @GetMapping("/event-associations")
    @ResponseBody
    public ResponseEntity<Object> checkImageEventAssociations() {
        try {
            Map<String, Object> results = new HashMap<>();
            
            // Collect all events with images
            List<Map<String, Object>> eventImageAssociations = new ArrayList<>();
            try {
                List<EventDto> events = eventEndpoint.getAll();  // Get all events
                for (EventDto event : events) {
                    Map<String, Object> eventInfo = new HashMap<>();
                    eventInfo.put("eventId", event.getId());
                    eventInfo.put("eventTitle", event.getTitle());
                    
                    if (event.getImage() != null) {
                        eventInfo.put("hasImage", true);
                        eventInfo.put("imageId", event.getImage().getId());
                        
                        // Try to find the image in the database
                        try {
                            Image image = imageService.get(event.getImage().getId());
                            eventInfo.put("fileName", image.getFileName());
                            eventInfo.put("contentType", image.getContentType());
                            eventInfo.put("hasImageData", image.getImageData() != null && !image.getImageData().isEmpty());
                            if (image.getImageData() != null) {
                                eventInfo.put("imageDataLength", image.getImageData().length());
                                // Try to decode the base64 data to verify it's valid
                                try {
                                    byte[] decodedData = Base64.getDecoder().decode(image.getImageData());
                                    eventInfo.put("decodedDataLength", decodedData.length);
                                    eventInfo.put("isValidBase64", true);
                                } catch (IllegalArgumentException e) {
                                    eventInfo.put("isValidBase64", false);
                                    eventInfo.put("base64Error", e.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            eventInfo.put("imageError", e.getMessage());
                            eventInfo.put("imageFound", false);
                        }
                    } else {
                        eventInfo.put("hasImage", false);
                    }
                    
                    eventImageAssociations.add(eventInfo);
                }
                results.put("events", eventImageAssociations);
                results.put("eventCount", eventImageAssociations.size());
            } catch (Exception e) {
                results.put("error", e.getMessage());
                log.error("Error checking event-image associations", e);
            }
            
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error in event-image association endpoint: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Event-image association endpoint error: " + e.getMessage()));
        }
    }

    @GetMapping("/check/{id}")
    @ResponseBody
    public ResponseEntity<Object> checkImage(@PathVariable Long id) {
        try {
            log.info("Checking image with ID: {}", id);
            Map<String, Object> result = new HashMap<>();
            
            try {
                Image image = imageService.get(id);
                result.put("exists", true);
                result.put("id", image.getId());
                result.put("fileName", image.getFileName());
                result.put("contentType", image.getContentType());
                result.put("hasImageData", image.getImageData() != null && !image.getImageData().isEmpty());
                if (image.getImageData() != null) {
                    result.put("imageDataLength", image.getImageData().length());
                    // Try to decode the base64 data to verify it's valid
                    try {
                        byte[] decodedData = Base64.getDecoder().decode(image.getImageData());
                        result.put("decodedDataLength", decodedData.length);
                        result.put("isValidBase64", true);
                    } catch (IllegalArgumentException e) {
                        result.put("isValidBase64", false);
                        result.put("base64Error", e.getMessage());
                    }
                }
                result.put("uploadedAt", image.getUploadedAt());
                
                // Check if this image is associated with any event
                boolean hasEvent = image.getEvent() != null;
                result.put("hasEventAssociation", hasEvent);
                if (hasEvent) {
                    result.put("eventId", image.getEvent().getId());
                    result.put("eventTitle", image.getEvent().getTitle());
                }
            } catch (Exception e) {
                result.put("exists", false);
                result.put("error", e.getMessage());
                log.error("Error checking image with ID {}: {}", id, e.getMessage());
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in image check endpoint: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Image check error: " + e.getMessage()));
        }
    }
} 