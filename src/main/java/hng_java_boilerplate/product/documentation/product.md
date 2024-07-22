
## **Product Search Endpoint**

### **Endpoint**

- **URL**: `/api/v1/products/search`
- **Method**: `GET`
- **Content-Type**: `application/json`

### **Description**

The `searchProducts` endpoint allows clients to search for products based on various criteria, including name, category, and price range. The endpoint returns a list of products that match the search criteria or a no-content status if no products are found.

### **Query Parameters**

- **`name`** (required)
    - **Description**: The name of the product to search for. This parameter is used to filter products based on their name.
    - **Type**: `String`
    - **Validation**:
        - **Required**: This parameter is mandatory and must be provided in the request.
        - **Trimmed**: Leading and trailing spaces are removed.
        - **Quotes**: Any surrounding quotes are removed.
        - **Minimum Length**: The name must be at least 1 character long after trimming.
    - **Example**: `example-product`

- **`category`** (optional)
    - **Description**: The category of the product to filter by. This parameter is used to filter products within a specific category.
    - **Type**: `String`
    - **Example**: `electronics`

- **`minPrice`** (optional)
    - **Description**: The minimum price of the product to filter by. This parameter is used to filter products that are priced at or above this value.
    - **Type**: `Double`
    - **Example**: `50.0`

- **`maxPrice`** (optional)
    - **Description**: The maximum price of the product to filter by. This parameter is used to filter products that are priced at or below this value.
    - **Type**: `Double`
    - **Example**: `200.0`

### **Validation Rules**

- **`name` Parameter**:
    - **Required**: Must be included in the request.
    - **Length**: Must not be empty after trimming.
    - **Formatting**: Leading and trailing spaces, as well as surrounding quotes, will be removed.

### **Responses**

#### **Success Response**

- **Status Code**: `200 OK`
- **Content-Type**: `application/json`
- **Response Body**:

  ```json
  {
    "status_code": 200,
    "products": [
      {
        "id": "1",
        "name": "Example Product",
        "category": "electronics",
        "price": 99.99
      }
      
    ],
    "success": true,
     "total": 10,
     "page": 0,
     "limit": 10
  }
  ```

  **Fields**:
    - `status_code`: The HTTP status code indicating the result of the request (200 for OK).
    - `products`: An array of product objects matching the search criteria. Each product object includes details such as `id`, `name`, `category`, and `price`.
    - `success`: A boolean indicating whether the request was successful.

#### **No Content Response**

- **Status Code**: `204 No Content`
- **Content-Type**: `application/json`
- **Response Body**:

  ```json
  {
    "status_code": 204,
    "products": [],
    "success": true
  }
  ```

  **Fields**:
    - `status_code`: The HTTP status code indicating no content was found (204).
    - `products`: An empty array indicating that no products matched the search criteria.
    - `success`: A boolean indicating that the request was processed successfully.

#### **Validation Error Response**

- **Status Code**: `422 Unprocessable Entity`
- **Content-Type**: `application/json`
- **Response Body**:

  ```json
  {
    "success": false,
    "errors": [
      {
        "parameter": "name",
        "message": "name is a required parameter"
      }
    ],
    "statusCode": 422
  }
  ```

  **Fields**:
    - `success`: A boolean indicating that the request failed due to validation errors.
    - `errors`: An array of error objects detailing the validation issues.
        - `parameter`: The name of the parameter that caused the error.
        - `message`: A description of the validation error.
    - `statusCode`: The HTTP status code indicating that the request was unprocessable (422).

### **Error Handling**

- **Bad Request (400 Bad Request)**: If the `name` parameter is missing or invalid (e.g., empty after trimming), the server responds with a 422 status code indicating unprocessable entity. The response will include an error message describing the validation issue.

### **Examples**

#### **Request Example**

```http
GET /api/v1/products/search?name=example-product&category=electronics&minPrice=50.0&maxPrice=200.0
```

#### **Successful Response Example**

```json
{
  "status_code": 200,
  "products": [
    {
      "id": "1",
      "name": "Example Product",
      "category": "electronics",
      "price": 99.99
    }
  ],
  "success": true,
  "total": 10,
  "page": 0,
  "limit": 10
}
```

#### **No Content Response Example**

```json
{
  "status_code": 204,
  "products": [],
  "success": true
}
```

#### **Validation Error Response Example**

```json
{
  "success": false,
  "errors": [
    {
      "parameter": "name",
      "message": "name is a required parameter"
    }
  ],
  "statusCode": 422
}
```
.