# Render Monitor System

The **Render Monitor System** is a lightweight monitoring service designed specifically to prevent **Render** projects from going to sleep due to inactivity.

Render automatically stops (sleeps) servers if they do not receive any requests for **15 minutes**. This system solves that problem by sending periodic requests to your API.

---

## System Concept

* The system sends **GET requests** to your project’s API endpoint.
* Requests are sent at a **random interval between 0 and 5 minutes**.
* This guarantees continuous traffic, keeping your Render service alive.

---

## How the System Works (Workflow)

1. The user creates a monitor for a specific website.
2. The system starts sending random GET requests to the provided URL.
3. The system continuously checks the website status.
4. Email notifications are sent when the website goes down or comes back up.
5. If a website stays down for more than one week, the monitor is deleted automatically.

---

## Create a New Monitor

To create a monitor, send a **POST request** to:

```
POST /
```

### Request Body (JSON)

```json
{
  "email": "String",
  "url": "String"
}
```

### What happens?

* A new monitor is created for the given URL.
* The system immediately starts sending GET requests to it.
* The system returns a **monitor ID**.

### ⚠️ Important Note

* **Each website (URL) can have only ONE monitor**.
* You **must store the returned monitor ID**.
* This ID is required for all future operations (update or delete).

---

## Update Monitor Email

To update the email associated with a monitor:

```
PUT /{id}
```

### Request Body (JSON)

```json
{
  "email": "String"
}
```

### Result

* The email stored in the system is updated.
* All future notifications will be sent to the new email address.

---

## Delete a Monitor

To delete an existing monitor:

```
DELETE /{id}
```

### Result

* The monitor is permanently deleted.
* The system stops sending requests to the website.
* You are free to create a **new monitor for the same website**.

---

## Email Notifications

The system sends automatic email notifications in the following cases:

* 📉 **Website goes down** → You receive an alert email.
* 📈 **Website comes back online** → You receive a recovery email.

---

## Automatic Cleanup

* If a website remains **offline for more than one full week**:

  * The monitor is deleted automatically.
  * This keeps the system clean and efficient.

---

## Use Cases

* Keep free Render projects always running.
* Monitor small APIs or backend services.
* Receive instant alerts when a service goes down.

---

## Final Notes

* Lightweight and efficient by design.
* No complex configuration required.
* Ideal for small to medium projects.

---

🚀 **Use it once and forget about Render sleep forever.**
