## Leave Endpoints

### Fetch All Leaves

#### Request
`GET` `/api/v1/leave/hr`

##### Request Body
###### No Request Body

#### Response

| Response Field | Type      | Description                          |
|----------------|-----------|--------------------------------------|
| totalCount     | Integer   | Total count of fetched leaves        |
| pageNumber     | Integer   | Page Number                          |
| id             | Long      | Leave ID                             |
| employeeName   | String    | Name of Employee who filed the leave |
| managerName    | String    | Name of Manager of the Employee      |
| startDate      | LocalDate | Date which the leave starts          |
| endDate        | LocalDate | Date which the leave end             |
| days           | Integer   | Number of days of the leave          |
| reason         | String    | Reason for leave                     |
| LeaveStatus    | String    | Status of the leave request          |


Status Code: `200 OK`

##### Response Body

Example:
```json
{
  "totalCount": 3,
  "pageNumber": 1,
  "content": [
    {
      "id": 1,
      "employeeName": "John Doe",
      "managerName": "Dexter Sy",
      "startDate": "2023-09-02",
      "endDate": "2023-09-07",
      "days": 4,
      "reason": "Sick Leave",
      "leaveStatus": "APPROVED"
    },
    {
      "id": 2,
      "employeeName": "Jane Doe",
      "managerName": "Verg Oplado",
      "startDate": "2023-09-02",
      "endDate": "2023-09-07",
      "days": 4,
      "reason": "Vacation Leave",
      "leaveStatus": "APPROVED"
    },
    {
      "id": 3,
      "employeeName": "Jane Doe",
      "managerName": "Verg Oplado",
      "startDate": "2023-10-02",
      "endDate": "2023-10-06",
      "days": 3,
      "reason": "Spa Leave",
      "leaveStatus": "REJECTED"
    }
  ]
}
```

### Fetch Own Leaves

#### Request
`GET` `/api/v1/leave?employeeId={employeeId}`

##### Request Body
###### No Request Body

#### Response

| Response Field | Type      | Description                          |
|----------------|-----------|--------------------------------------|
| totalCount     | Integer   | Total count of fetched leaves        |
| pageNumber     | Integer   | Page Number                          |
| id             | Long      | Leave ID                             |
| employeeName   | String    | Name of Employee who filed the leave |
| managerName    | String    | Name of Manager of the Employee      |
| startDate      | LocalDate | Date which the leave starts          |
| endDate        | LocalDate | Date which the leave end             |
| days           | Integer   | Number of days of the leave          |
| reason         | String    | Reason for leave                     |
| LeaveStatus    | String    | Status of the leave request          |


Status Code: `200 OK`

##### Response Body

Example:
```json
{
  "totalCount": 2,
  "pageNumber": 1,
  "content": [
    {
      "id": 1,
      "employeeName": "John Doe",
      "managerName": "Verg Oplado",
      "startDate": "2023-09-02",
      "endDate": "2023-09-07",
      "days": 4,
      "reason": "Sick Leave",
      "leaveStatus": "APPROVED"
    },
    {
      "id": 2,
      "employeeName": "John Doe",
      "managerName": "Verg Oplado",
      "startDate": "2023-11-01",
      "endDate": "2023-11-03",
      "days": 2,
      "reason": "All Saints Day",
      "leaveStatus": "PENDING"
    }
  ]
}
```

### Fetch Leaves Under Manager

#### Request
`GET` `/api/v1/leave/manager?managerId={managerId}`

##### Request Body
###### No Request Body

#### Response

| Response Field | Type      | Description                          |
|----------------|-----------|--------------------------------------|
| totalCount     | Integer   | Total count of fetched leaves        |
| pageNumber     | Integer   | Page Number                          |
| id             | Long      | Leave ID                             |
| employeeName   | String    | Name of Employee who filed the leave |
| managerName    | String    | Name of Manager of the Employee      |
| startDate      | LocalDate | Date which the leave starts          |
| endDate        | LocalDate | Date which the leave end             |
| days           | Integer   | Number of days of the leave          |
| reason         | String    | Reason for leave                     |
| LeaveStatus    | String    | Status of the leave request          |


Status Code: `200 OK`

##### Response Body

Example:
```json
{
  "totalCount": 2,
  "pageNumber": 1,
  "content": [
    {
      "id": 1,
      "employeeName": "John Doe",
      "managerName": "Verg Oplado",
      "startDate": "2023-09-02",
      "endDate": "2023-09-07",
      "days": 4,
      "reason": "Sick Leave",
      "leaveStatus": "APPROVED"
    },
    {
      "id": 2,
      "employeeName": "Jane Doe",
      "managerName": "Verg Oplado",
      "startDate": "2023-11-01",
      "endDate": "2023-11-03",
      "days": 2,
      "reason": "All Saints Day",
      "leaveStatus": "PENDING"
    }
  ]
}
```

### Create Leave

#### Request
`POST` `/api/v1/leave`

##### Request Body

| Request Field | Type      | Required  | Description                 |
|---------------|-----------|-----------|-----------------------------|
| employee_id   | Long      | true      | Employee ID                 |
| startDate     | LocalDate | true      | Date which the leave starts |
| endDate       | LocalDate | true      | Date which the leave end    |
| reason        | String    | true      | Reason for leave            |

Example:
```json
{
  "employee_id": 1,
  "startDate" : "2023-09-02",
  "endDate" : "2023-09-07",
  "reason" : "Vacation Leave"
}
```
#### Response

| Request Field | Type      | Description                          |
|---------------|-----------|--------------------------------------|
| id            | Long      | Leave ID                             |
| employeeName  | String    | Name of Employee who filed the leave |
| managerName   | String    | Name of Manager of the Employee      |
| startDate     | LocalDate | Date which the leave starts          |
| endDate       | LocalDate | Date which the leave end             |
| days          | Integer   | Number of days of the leave          |
| reason        | String    | Reason for leave                     |
| LeaveStatus   | String    | Status of the leave request          |


Status Code: `201 Created`

##### Response Body

Example:
```json
{
  "id": 1,
  "employeeName": "John Doe",
  "managerName": "Dexter Sy",
  "startDate": "2023-09-02",
  "endDate": "2023-09-07",
  "days": 4,
  "reason": "Vacation Leave",
  "leaveStatus": "PENDING"
}
```

### Approve Leave

#### Request
`PUT` `/api/v1/leave/approve/{id}`

##### Request Body
###### No Request Body
#### Response

| Request Field | Type      | Description                          |
|---------------|-----------|--------------------------------------|
| id            | Long      | Leave ID                             |
| employeeName  | String    | Name of Employee who filed the leave |
| managerName   | String    | Name of Manager of the Employee      |
| startDate     | LocalDate | Date which the leave starts          |
| endDate       | LocalDate | Date which the leave end             |
| days          | Integer   | Number of days of the leave          |
| reason        | String    | Reason for leave                     |
| LeaveStatus   | String    | Status of the leave request          |


Status Code: `202 Accepted`

##### Response Body

Example:
```json
{
  "id": 1,
  "employeeName": "John Doe",
  "managerName": "Dexter Sy",
  "startDate": "2023-09-02",
  "endDate": "2023-09-07",
  "days": 4,
  "reason": "Vacation Leave",
  "leaveStatus": "APPROVED"
}
```


### Reject Leave

#### Request
`PUT` `/api/v1/leave/reject/{id}`

##### Request Body
###### No Request Body
#### Response

| Request Field | Type      | Description                          |
|---------------|-----------|--------------------------------------|
| id            | Long      | Leave ID                             |
| employeeName  | String    | Name of Employee who filed the leave |
| managerName   | String    | Name of Manager of the Employee      |
| startDate     | LocalDate | Date which the leave starts          |
| endDate       | LocalDate | Date which the leave end             |
| days          | Integer   | Number of days of the leave          |
| reason        | String    | Reason for leave                     |
| LeaveStatus   | String    | Status of the leave request          |


Status Code: `202 Accepted`

##### Response Body

Example:
```json
{
  "id": 1,
  "employeeName": "John Doe",
  "managerName": "Dexter Sy",
  "startDate": "2023-09-02",
  "endDate": "2023-09-07",
  "days": 4,
  "reason": "Vacation Leave",
  "leaveStatus": "REJECTED"
}
```


### Cancel Leave

#### Request
`DELETE` `/api/v1/leave/{id}`

##### Request Body
###### No Request Body

Status Code: `204 No Content`

#### Response
###### No Response Body



