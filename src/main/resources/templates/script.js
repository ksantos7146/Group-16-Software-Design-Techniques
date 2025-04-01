
// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM fully loaded and parsed');
    
    // Initialize tooltips
    initTooltips();
    
    // Initialize form validation
    initFormValidation();
    
    // Initialize event handlers
    initEventHandlers();
    
    // Initialize map if the map container exists
    if (document.getElementById('map-container')) {
        initMap();
    }
    
    // Initialize date pickers
    initDatePickers();
});

/**
 * Initialize Bootstrap tooltips
 */
function initTooltips() {
    // Select all elements with data-bs-toggle="tooltip" attribute
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    
    // Create tooltip instances
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Initialize form validation
 */
function initFormValidation() {
    // Get all forms with the 'needs-validation' class
    const forms = document.querySelectorAll('.needs-validation');
    
    // Loop over them and prevent submission
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            
            form.classList.add('was-validated');
        }, false);
    });
}

/**
 * Initialize event handlers
 */
function initEventHandlers() {
    // Category filter buttons
    const categoryButtons = document.querySelectorAll('.category-filter');
    if (categoryButtons.length > 0) {
        categoryButtons.forEach(button => {
            button.addEventListener('click', function() {
                const category = this.getAttribute('data-category');
                filterEventsByCategory(category);
            });
        });
    }
    
    // Search form
    const searchForm = document.getElementById('search-form');
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const searchQuery = document.getElementById('search-input').value;
            searchEvents(searchQuery);
        });
    }
    
    // Registration button
    const registerButton = document.getElementById('register-button');
    if (registerButton) {
        registerButton.addEventListener('click', function() {
            const eventId = this.getAttribute('data-event-id');
            registerForEvent(eventId);
        });
    }
    
    // Cancel registration button
    const cancelButton = document.getElementById('cancel-registration-button');
    if (cancelButton) {
        cancelButton.addEventListener('click', function() {
            const eventId = this.getAttribute('data-event-id');
            cancelRegistration(eventId);
        });
    }
}

/**
 * Filter events by category
 * @param {string} category - The category to filter by
 */
function filterEventsByCategory(category) {
    console.log('Filtering events by category:', category);
    
    // Get all event cards
    const eventCards = document.querySelectorAll('.event-card');
    
    // Show all events if 'all' category is selected
    if (category === 'all') {
        eventCards.forEach(card => {
            card.style.display = 'block';
        });
        return;
    }
    
    // Filter events by category
    eventCards.forEach(card => {
        const cardCategory = card.getAttribute('data-category');
        if (cardCategory === category) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

/**
 * Search events
 * @param {string} query - The search query
 */
function searchEvents(query) {
    console.log('Searching events:', query);
    
    // In a real app, this would make an AJAX request to the server
    // For now, we'll just simulate a search by filtering the events on the page
    
    // Get all event cards
    const eventCards = document.querySelectorAll('.event-card');
    
    // Show all events if query is empty
    if (!query.trim()) {
        eventCards.forEach(card => {
            card.style.display = 'block';
        });
        return;
    }
    
    // Filter events by title or description
    const lowerCaseQuery = query.toLowerCase();
    eventCards.forEach(card => {
        const title = card.querySelector('.card-title').textContent.toLowerCase();
        const description = card.querySelector('.card-text') ? 
                           card.querySelector('.card-text').textContent.toLowerCase() : '';
        
        if (title.includes(lowerCaseQuery) || description.includes(lowerCaseQuery)) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

/**
 * Register for an event
 * @param {string} eventId - The ID of the event
 */
function registerForEvent(eventId) {
    console.log('Registering for event:', eventId);
    
    // In a real app, this would make an AJAX request to the server
    // For now, we'll just show a success message
    
    // Check if the user is logged in
    const isLoggedIn = checkUserLoggedIn();
    
    if (!isLoggedIn) {
        // Redirect to login page
        window.location.href = 'login.html?redirect=event-detail.html?id=' + eventId;
        return;
    }
    
    // Show a success message
    const messageContainer = document.getElementById('message-container');
    if (messageContainer) {
        messageContainer.innerHTML = '<div class="alert alert-success">You have successfully registered for this event!</div>';
        
        // Update the UI to show that the user is registered
        const registerButton = document.getElementById('register-button');
        if (registerButton) {
            registerButton.textContent = 'Registered';
            registerButton.classList.remove('btn-primary');
            registerButton.classList.add('btn-success');
            registerButton.disabled = true;
        }
    }
}

/**
 * Cancel registration for an event
 * @param {string} eventId - The ID of the event
 */
function cancelRegistration(eventId) {
    console.log('Canceling registration for event:', eventId);
    
    // In a real app, this would make an AJAX request to the server
    // For now, we'll just show a success message
    
    // Show a success message
    const messageContainer = document.getElementById('message-container');
    if (messageContainer) {
        messageContainer.innerHTML = '<div class="alert alert-info">Your registration has been canceled.</div>';
        
        // Update the UI to show that the user is not registered
        const cancelButton = document.getElementById('cancel-registration-button');
        if (cancelButton) {
            cancelButton.style.display = 'none';
            
            const registerButton = document.getElementById('register-button');
            if (registerButton) {
                registerButton.textContent = 'Register';
                registerButton.classList.remove('btn-success');
                registerButton.classList.add('btn-primary');
                registerButton.disabled = false;
                registerButton.style.display = 'block';
            }
        }
    }
}

/**
 * Check if the user is logged in
 * @returns {boolean} - Whether the user is logged in
 */
function checkUserLoggedIn() {
    // In a real app, this would check if the user is logged in
    // For now, we'll just return false
    return false;
}

/**
 * Initialize the map
 */
function initMap() {
    console.log('Initializing map');
    
    // In a real app, this would initialize a map using a mapping API like Google Maps or Leaflet
    // For now, we'll just show a placeholder
    
    const mapContainer = document.getElementById('map-container');
    if (mapContainer) {
        mapContainer.innerHTML = '<div class="bg-light d-flex align-items-center justify-content-center" style="height: 300px;"><p class="text-muted">Map would be displayed here</p></div>';
    }
}

/**
 * Initialize date pickers
 */
function initDatePickers() {
    // In a real app, this would initialize date pickers using a library like Flatpickr or Bootstrap Datepicker
    // For now, we'll just use the browser's native date input
    
    const dateInputs = document.querySelectorAll('input[type="date"]');
    dateInputs.forEach(input => {
        // Set min date to today
        const today = new Date().toISOString().split('T')[0];
        input.setAttribute('min', today);
    });
}

/**
 * Format a date
 * @param {Date} date - The date to format
 * @returns {string} - The formatted date
 */
function formatDate(date) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString('en-US', options);
}

/**
 * Format a time
 * @param {Date} date - The date to format
 * @returns {string} - The formatted time
 */
function formatTime(date) {
    const options = { hour: 'numeric', minute: 'numeric', hour12: true };
    return date.toLocaleTimeString('en-US', options);
}
