package com.main.easy2learnproject.Calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }


    private String name;
    private LocalDate date;
    private String time;
    private String location;

    private String title;

    private String locStr;
    public Event(String name, LocalDate date, String time, String location, String locStr, String title)
    {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.locStr = locStr;
        this.title = title;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public void setLocation(String location){this.location = location;}

    public String getLocation(){return location;}

    public String getLocStr() {
        return locStr;
    }

    public void setLocStr(String locStr) {
        this.locStr = locStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}