**Notification Delivery Engine**
**Overview:**
This project is a multi-channel notification delivery system built using Spring Boot, allowing administrators/users to create their entry and register them as users. The Administrator only has the access to send notifications via Email or WhatsApp to users. The system supports user preferences, scheduling notifications, and tracking delivery status, with optional authentication for secure endpoints.

**Features:**
The system allows administrators to create users through a public endpoint without requiring authentication, enabling seamless onboarding. For sending notifications and checking delivery status, JWT-based authentication is implemented to secure sensitive operations. Notifications can be sent immediately through /admin/send-notification or scheduled for a specific time using /admin/schd-notification. Scheduled notifications are handled asynchronously via Spring Boot’s TaskScheduler, ensuring that users receive messages at the specified time according to their preferred channel. The system tracks each notification’s lifecycle, moving through CREATED → SENT, providing full visibility into delivery outcomes.

**Authentication and Scheduler Design:**
JWT-based authentication is applied to endpoints that modify or access sensitive data, such as sending notifications and retrieving status. Public endpoints like /admin/create-user do not require authentication to allow smooth user creation. Scheduling endpoints let administrators send notifications at a future time, respecting user preferences for channel delivery. Asynchronous scheduling ensures other operations are not blocked and notifications are delivered reliably.

**Execution Flow:**

1. Create User (/admin/create-user)
The controller receives a user DTO → UserService saves it → NotificationService sends notification immediately via the user’s preferred channel → Delivery history is recorded. No authentication required.

2. Admin Login (/admin/auth/login)
Admin sends credentials → AdminAuthController validates using PasswordEncoder → JWT token generated for subsequent requests.

3. Send Notification (/admin/send-notification)
Controller validates JWT → fetches user → NotificationService sends notification via correct channel → Delivery history updated → Response returned.

4. Scheduled Notification (/admin/schd-notification)
Controller validates JWT → fetches user → NotificationService creates a notification in CREATED state → TaskScheduler schedules a task → At scheduled time, system determines channel → sends notification via EmailChannel or WhatsAppChannel → updates delivery status → history saved.

5. Delivery Tracking (/admin/status/{notificationId})
Controller validates JWT → NotificationService fetches the latest delivery status from DeliveryHistory → returns status.

**Getting Started:**

Note: In setup, update the twilio SID , twilio token and SMTP Gmail from id and app password in application.properties file to get started with the application.

Build the application: ./mvnw clean install -DskipTests

Run the application and send request to the endpoints:

1. create-user:
	URL: http://localhost:8081/admin/create-user
	BODY:
		{
			"username": "hari",
			"userEmailId": "abc@gmail.com",
			"userMobileNumber": "+911234567891",
			"userChannelType": "WHATSAPP"
		}
	RESPONSE:
		{
			"message": "User created successfully and notification sent",
			"notificationId": "576afa36-384c-492b-b9a6-a59e8edc5210"
		}
	[Verify the notification recieved on the preferred channel type, for EMAIL upated the BODY userChannelType to 'EMAIL']
	
2. admin-login:
	URL: http://localhost:8081/admin/auth/login
	BODY:
		{
			"username": "Admin@123",
			"password": "Admin@123"
		}
		
	RESPONSE: Bearer Auth Token
	
3. send-notification:
	URL: http://localhost:8081/admin/send-notification
	AUTHORIZATION: Bearer <token> [Generated from admin-login endpoint]
	BODY:
		{
			"username": "balaji",
			"message": "Your order has been shipped successfully."
		}
	RESPONSE:
		{
			"message": "Notification sent successfully",
			"notificationId": "5b0dda03-51c1-4fb3-aa73-9b6da6e8d386"
		}
		
4. get-status:
	URL: http://localhost:8081/admin/status/{notification id from send-notification/create-user endpoint}
	AUTHORIZATION: Bearer <token> [Generated from admin-login endpoint]
	RESPONSE:
		{
			"status": "SENT",
			"time-stamp": "2026-02-15T11:30:37.679+00:00"
		}

5. scheduled-notification:
	URL: http://localhost:8081/admin/schd-notification
	AUTHORIZATION: Bearer <token> [Generated from admin-login endpoint]
	BODY:
		{
			"username": "hari",
			"message": "Hello! This is a scheduled notification.",
			"scheduleTime": "2026-02-15T17:25:00"
		}
	RESPONSE:
		{
			"message": "Notification scheduled successfully",
			"notificationId": "1f80164f-1000-4e4a-a776-40ccb8ac43e3"
		}
	

