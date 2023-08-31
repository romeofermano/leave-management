## Leave Endpoints

### Get All Employees

#### Request

`GET` `/api/v1/employees`

##### Request Body

###### No Request Body

#### Response

| Response Field | Type           | Description                |
|----------------|----------------|----------------------------|
| id             | Long           | Employee ID                |
| name           | String         | Employee Name              |
| roleType       | RoleType       | Role Type of Employee      |                                                                                                                                                                                                                                               
| totalLeaves    | Integer        | Total Leaves of Employee   |
| currentLeaves  | Integer        | Current Leaves of Employee |
| employeeStatus | EmployeeStatus | Status of Employee         |                   
| manager        | Employee       | Manager of Employee        | 

Status Code: `200 OK`

##### Response Body

Example:

```json
{
  "totalCount": 12,
  "pageNumber": 1,
  "content": [
    {
      "id": 2,
      "name": "Romeo Fermano",
      "roleType": "MANAGER",
      "totalLeaves": 30,
      "currentLeaves": 30,
      "employeeStatus": "ACTIVE",
      "manager": "HR Admin"
    },
    {
      "id": 5,
      "name": "Ernest Dylan Gloria",
      "roleType": "MEMBER",
      "totalLeaves": 30,
      "currentLeaves": 30,
      "employeeStatus": "ACTIVE",
      "manager": "Romeo Fermano"
    }
  ]
}
```

### Get Employee List

#### Request

`GET` `/api/v1/employees/list`

##### Request Body

###### No Request Body

#### Response

| Response Field | Type     | Description           |
|----------------|----------|-----------------------|
| id             | Long     | Employee ID           |
| name           | String   | Employee Name         |
| roleType       | RoleType | Role Type of Employee |

Status Code: `200 OK`

##### Response Body

Example:

```json
[
  {
    "id": 1,
    "name": "HR Admin",
    "roleType": "HR_ADMIN"
  },
  {
    "id": 2,
    "name": "Romeo Fermano",
    "roleType": "MANAGER"
  },
  {
    "id": 5,
    "name": "Ernest Dylan Gloria",
    "roleType": "MEMBER"
  }
]
```

### Get Employee

#### Request

`GET` `/api/v1/employees/{id}`

##### Request Body

###### No Request Body

#### Response

| Response Field | Type           | Description                |
|----------------|----------------|----------------------------|
| id             | Long           | Employee ID                |
| name           | String         | Employee Name              |
| roleType       | RoleType       | Role Type of Employee      |                                                                                                                                                                                                                                               
| totalLeaves    | Integer        | Total Leaves of Employee   |
| currentLeaves  | Integer        | Current Leaves of Employee |
| employeeStatus | EmployeeStatus | Status of Employee         |                   
| manager        | Employee       | Manager of Employee        | 

Status Code: `200 OK`

##### Response Body

Example:

```json
{
  "id": 12,
  "name": "Ernest Dylan Gloria",
  "roleType": "MEMBER",
  "totalLeaves": 30,
  "currentLeaves": 30,
  "employeeStatus": "ACTIVE",
  "manager": "Romeo Fermano"
}
```

### Create Employee

#### Request

`POST` `/api/v1/employees?adminId={id}`

##### Request Body

| Request Field | Type     | Required | Description                 |
|---------------|----------|----------|-----------------------------|
| name          | String   | true     | Employee ID                 |
| roleType      | RoleType | true     | Date which the leave starts |
| totalLeaves   | Integer  | true     | Date which the leave end    |
| managerId     | Long     | true     | Reason for leave            |

Example:

```json
 {
  "name": "Jhon Cena",
  "roleType": "MEMBER",
  "totalLeaves": 30,
  "managerId": 2
}
```

#### Response

| Response Field | Type           | Description                |
|----------------|----------------|----------------------------|
| id             | Long           | Employee ID                |
| name           | String         | Employee Name              |
| roleType       | RoleType       | Role Type of Employee      |                                                                                                                                                                                                                                               
| totalLeaves    | Integer        | Total Leaves of Employee   |
| currentLeaves  | Integer        | Current Leaves of Employee |
| employeeStatus | EmployeeStatus | Status of Employee         |                   
| manager        | Employee       | Manager of Employee        |

Status Code: `201 Created`

##### Response Body

Example:

```json
{
  "id": 15,
  "name": "Jhon Cena",
  "roleType": "MEMBER",
  "totalLeaves": 30,
  "currentLeaves": 30,
  "employeeStatus": "ACTIVE",
  "manager": "Romeo Fermano"
}
```