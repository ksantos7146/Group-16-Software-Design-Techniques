-- Update the admin user's password to a BCrypt-encoded version
-- The encrypted value corresponds to password "1"
UPDATE users
SET password = '$2a$10$N.4P32iB5XuRX0GiKDXKiu3vGOJYaTl9HUG5V9nU.M.YGWX6T27Te'
WHERE username = 'admin'; 