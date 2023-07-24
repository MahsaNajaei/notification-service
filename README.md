# Administration Service

As the name suggests, this service is responsible for
notifying users and admins when a notification request is sent by other services
using “RestTemplate”. Reactor RabbitMQ is used for this purpose to generate a messaging
queue for each client and client subscribes associated notifications by calling
an API of notification service. In this phase of project, no database is used
to store notification data but a history of notification info is required in
real world to handle unsuccessful message deliveries and other data based on
application needs.
 
## Reference Documentation

View complete documentation in the following link:

* [Notification Service Documentation](https://documenter.getpostman.com/view/22527751/2s946o38rB)

