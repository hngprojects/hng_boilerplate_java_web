
## **Endpoint for Sending SMSes Using Twilio API**

### **Endpoint**

- **URL**: `/api/v1/sms/send`
- **Method**: `POST`
- **Content-Type**: `application/json`

### **Description**



The `sendSMS` endpoint allows to send SMS messages via Twilio. It requires a valid JWT token for authentication and accepts parameters for the recipient's phone number and message content. The endpoint returns a success status with details if the SMS is sent successfully or an error status if the phone number or message content is invalid, or if there is a failure in sending the SMS.
### **Request Body**

- phone_number (required)

   - Description: The recipient's phone number to which the SMS will be sent. This parameter is used to specify the destination of the SMS.
   - Type: String
   - Validation:
     - Required: This parameter is mandatory and must be provided in the request body.
     - Format: Must be a valid phone number format, including the country code.
Example: +1234567890
     

- message (required)

   - Description: The content of the SMS to be sent. This parameter is used to specify the text of the message.
   - Type: String
   - Validation:
      - Required: This parameter is mandatory and must be provided in the request.
       
        Example: Hello, this is a test message!


### **Validation Rules**

- **`phone_number` Parameter**:
    - **Required**: Must be included in the request body.
    - **Length**: Must not be null or empty.
    - **format**: Must be a valid phone number format, including the country code.


- **`message` Parameter**:
    - **Required**: Must be included in the request body.
    - **Length**: Must not be empty.

### **Responses**

#### **Success Response**

- **Status Code**: `200 OK`
- **Content-Type**: `application/json`


- **Response Body**:

  ```json
  {
  "status": "success",
  "status_code": 200,
  "message": "SMS sent successfully."
}
 

  **Fields**:

    - success: A string indicating the request was processed successfully.
    - status_code: The HTTP status code indicating the request was a success (200).
    - message: A text that explains the status of the request.

#### **Validation Error Response**

- **Status Code**: `400 Bad Request`
- **Content-Type**: `application/json`


- **Response Body**:

  ```json
  {
  "status": "unsuccessful",
  "status_code": 400,
  "message": "Valid phone number and message content must be provided."
}

**Fields**:  
```
    - unsuccessful: A string indicating the request was unsuccessful.
    - status_code: The HTTP status code indicating the request was a bad request (400).
    - message: A text that explains on the status of the request.
```


#### **Invalid Twilio credentials or Network issue**

- **Status Code**: `500 Internal server error`
- **Content-Type**: `application/json`


- **Response Body**:

```json
{
  "status": "unsuccessful",
  "status_code": 500,
  "message": "Failed to send SMS. Please try again later."
}
```

**Fields**:
```
    - unsuccessful: A string indicating that the request was unsuccessful.
    - status_code: The HTTP status code indicating the request was an internal server error (500).
    - message: A text that explains the status of the request.
```