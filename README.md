# Products

### About 💬️

In this project I have implemented authentication with JWT to work with 
products: add a list of products and view a list of all products.

Also in this project you can see end-to-end testing of all endpoints 
with exception handling.

<ins>Stack: Spring Boot, Spring Data Jpa, MySQL, Spring Security, JUnit</ins>

### Configuration⚙️

For the correct operation of this project, you need to perform one of the following actions:

- Add environment variables with keys provided in application.properties,
for example: `${MYSQL_URL}`;
- Replace these variables with the required value, for example:
`spring.datasource.url=jdbc:mysql://localhost:3306/products`

Using the **first** method, you need to specify the following environment variables:

- MYSQL_URL;
- MYSQL_USERNAME;
- MYSQL_PASSWORD;
- SECRET_KEY;

For this you need:
1. Go to Run/Debug Configuration;
2. Choose Spring Boot project;
3. Press Modify options or Alt + M;
4. Add environment variables
5. Provide environment variables with values;

Using the **second** method, you simply need to replace all of the above variables with
required values, but remember that if you send your code to someone else, everyone
will see these secret values. Even if you create a new commit and replace it with
other values, the git history will remain.

### Test 🧪

*For testing, you need to first configure the app as mentioned above.*

Add user: ***POST /user/add***

JSON Body: 
```
{
    "username": "user",
    "password": "user"
}
```

Authenticate: ***POST /user/authenticate***

JSON Body: 
```
{
    "username": "user",
    "password": "user"
}
```

Get list of all products in database: ***GET /products/all***

Authorization: Bearer. Token, which you can receive using first two endpoints.

Add list of new records: ***POST /products/add***

Authorization: Bearer.

JSON Body: 
```
{
    "records": [
        {
            "entryDate": "03-01-2023",
            "itemCode": "11111",
            "itemName": "Test Inventory 1",
            "itemQuantity": "20",
            "status": "Paid"
        },
        {
            "entryDate": "03-01-2023",
            "itemCode": "11111",
            "itemName": "Test Inventory 2",
            "itemQuantity": "20",
            "status": "Paid"
        }
    ]
}
```