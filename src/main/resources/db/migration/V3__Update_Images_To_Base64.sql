-- Rename file_path to file_name
ALTER TABLE images CHANGE COLUMN file_path file_name VARCHAR(255) NOT NULL;

-- Add image_data column to store base64 content
ALTER TABLE images ADD COLUMN image_data LONGTEXT;

-- Add content_type column
ALTER TABLE images ADD COLUMN content_type VARCHAR(100); 