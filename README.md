# My Expenses Backend

My Expenses Backend is a RESTful API that allows users to manage their expenses. It provides endpoints for user
registration, authentication, and expense management.

- [Endpoints](#endpoints)
    - [Authentication](#authentication)
        - [Register](#register)
        - [Login](#login)
        - [Refresh Token](#refresh-token)
    - [Financial Records](#financial-records)
        - [Get Financial Record Categories](#get-financial-record-categories)
        - [Get Financial Records](#get-financial-records)
        - [Create Financial Record](#create-financial-record)
        - [Delete Financial Record](#delete-financial-record)
    - [Payments](#payments)
        - [Get Payments](#get-payments)
        - [Get Public Key](#get-public-key)
        - [Create Payment Intent](#create-payment-intent)
        - [Handle Payment Webhook](#handle-payment-webhook)
    - [User Management](#user-management)
        - [Delete User (Self)](#delete-user-self)
        - [Delete User by UUID](#delete-user-by-uuid)
    - [Roles and Permissions](#roles-and-permissions)
        - [Add Role to User](#add-role-to-user)
        - [Remove Role from User](#remove-role-from-user)

## Endpoints

- [`/api/v1/auth`](#authentication)
- [`/api/v1/financial-records`](#financial-records)
- [`/api/v1/payments`](#payments)
- [`/api/v1/users`](#user-management)
- [`/api/v1/roles`](#roles-and-permissions)

### Authentication

- [`POST /api/v1/auth/register`](#register): Register a new user.
- [`POST /api/v1/auth/login`](#login): Authenticate a user and return a JWT token.
- [`POST /api/v1/auth/refresh`](#refresh-token): Refresh the JWT token.

#### Register

**Request Body:**

```json
{
  "username": "string",
  "password": "string",
  "email": "string"
}
```

**Response:**

```json
{
  "token": "string"
}
```

![Register](/images/register.png)

#### Login

**Request Body:**

```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**

```json
{
  "token": "string"
}
```

![Login](/images/login.png)

#### Refresh Token

**Request Body:**

```json
{
  "refreshToken": "string"
}
```

**Response:**

```json
{
  "token": "string"
}
```

![Refresh](/images/refresh.png)

### Financial Records

- [`GET /api/v1/financial-records/categories`](#get-financial-record-categories): Get financial record categories.
- [`GET /api/v1/financial-records`](#get-financial-records): Get financial records.
- [`PUT /api/v1/financial-records`](#create-financial-record): Create a financial record.
- [`DELETE /api/v1/financial-records/{uuid}`](#delete-financial-record): Delete a financial record.

#### Get Financial Record Categories

**Response:**

```json
[
  {
    "name": "string"
  }
]
```

#### Get Financial Records

**Response:**

```json
[
  {
    "uuid": "string",
    "title": "string",
    "value": 0.0,
    "category": "string",
    "created_at": "2023-10-01T00:00:00Z"
  }
]
```

![Get Financial Records](/images/get_records.png)

#### Create Financial Record

**Request Body:**

```json
{
  "title": "string",
  "value": 0.0,
  "category": "string"
}
```

**Response:**

```json
{
  "uuid": "string",
  "title": "string",
  "value": 0.0,
  "category": "string",
  "created_at": "2023-10-01T00:00:00Z"
}
```

#### Delete Financial Record

![Delete Financial Record](/images/delete_record.png)

### Payments

- [`GET /api/v1/payments`](#get-payments): Get payments.
- [`GET /api/v1/payments/public-key`](#get-public-key): Get the public key for payment processing.
- [`POST /api/v1/payments/payment-intent`](#create-payment-intent): Create a payment-intent.
- [`POST /api/v1/payments/webhook`](#handle-payment-webhook): Handle payment webhook events.

#### Get Payments

**Response:**

```json
[
  {
    "paymentIntentId": "string",
    "status": "string",
    "createdAt": 0.0,
    "updatedAt": "2023-10-01T00:00:00Z"
  }
]
```

#### Get Public Key

**Response:**

```json
"string"
```

#### Create Payment Intent

**Request Body:**

```json
{
  "productId": "string"
}
```

**Response:**

```json
{
  "clientSecret": "string",
  "ephemeralKey": "string",
  "customerId": "string"
}
```

#### Handle Payment Webhook

It is an endpoint for Stripe to send webhook events. It processes events like payment success or failure.

### User Management

- [`DELETE /api/v1/users`](#delete-user-self): Deletes user (self).
- [`DELETE /api/v1/users/{uuid}`](#delete-user-by-uuid): Deletes user by UUID (needs user to have `ADMIN` role).

#### Delete User (Self)

This endpoint allows a user to delete their own account.

#### Delete User by UUID

This endpoint allows an admin to delete a user by their UUID.

### Roles and Permissions

All endpoints under this section require the user to have the `ADMIN` role.

- [`PUT /api/v1/roles/add`](#add-role-to-user): Add a new role to a user.
- [`DELETE /api/v1/roles/remove`](#remove-role-from-user): Remove a role from a user.

#### Add Role to User

**Request Body:**

```json
{
  "role": "string",
  "userUuid": "string"
}
```

![Add Role to User](/images/add_role.png)

#### Remove Role from User

**Request Body:**

```json
{
  "role": "string",
  "userUuid": "string"
}
```

![Remove Role from User](/images/remove_role.png)
