package com.interview.ceg.store;

import com.interview.ceg.dto.Event;
import com.interview.ceg.dto.EventType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventStore {

    private Map<EventType, LinkedList<Event>> store;

    public EventStore() {
        this.store = new ConcurrentHashMap<>();
    }

    public int store(Event event) {
        return store.compute(event.getMetadata().getEventType(), (eventType, currentEvents) -> {
            if (currentEvents == null) {
                currentEvents = new LinkedList<>();
            }
            currentEvents.add(event);
            return currentEvents;
        }).size();
    }

    public List<Event> remove(EventType eventType, int count) {
        List<Event> events = new ArrayList<>(count);
        store.computeIfPresent(eventType, (key, currentEvents) -> {
            int i = 0;
            while(i < count && currentEvents.size() > 0) {
                events.add(currentEvents.remove());
            }
            return currentEvents;
        });
        return events;
    }

}
