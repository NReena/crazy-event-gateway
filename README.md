# crazy-event-gateway
POST: http://localhost:8080/{event-type}/event
JSON:
{
"metadata": {
"event_id": "some-uniq-event-id",
"event_type": "EventType1" // should be same as the path param event-type
},
"data": {
"payload": {
// any json
}
}
}
Response:
Status Code: 201 // for success
Status Code: 509 // If exceeds the rate limitation
Status Code: 404 // If the path param event-type is not valid
Status Code: 400 // Validation Error
Body: { "message": "<What is wrong in the message>"}

Build: ./mvnw clean install
Run: java -jar target/crazy-event-gateway-0.0.1-SNAPSHOT.jar 
