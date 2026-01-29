package com.DigitalClassRoomManagement.ServiceImpl;



import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Service.CalendarService;
import com.DigitalClassRoomManagement.Dto.*;
import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final AcademicCalendarRepository calendarRepo;
    private final EventRepository eventRepo;
    private final HolidayRepository holidayRepo;
    private final KafkaTemplate<String, KafkaNotificationDto> kafkaTemplate;


    @Override
    public CalendarDto createAcademicCalender(CreateAcademicCalenderRequest request, String username) {
        AcademicCalendar calendar = AcademicCalendar.builder()
                .academicYear(request.getAcademicYear())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdBy(username)
                .build();
        calendarRepo.save(calendar);
        return mapToDto(calendar);
    }

    @Override
    public HolidayDto addHoliday(Long calendarId, Holiday holiday) {
        try {
            AcademicCalendar calendar = calendarRepo.findById(calendarId).orElseThrow();
            holiday.setCalendar(calendar);
            holidayRepo.save(holiday);
            return mapHolidayToDto(holiday);
        }catch(RuntimeException e){
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public EventDto addEvent(Long calendarId, Event event) {
        AcademicCalendar calendar = calendarRepo.findById(calendarId).orElseThrow();
        event.setCalendar(calendar);
        eventRepo.save(event);
        return mapEventToDto(event);
    }

    @Override
    public List<EventDto> viewCalenderEvents() {
        return eventRepo.findAll().stream().map(this::mapEventToDto).collect(Collectors.toList());
    }

    @Override
    public CalendarDto updateAcademicCalender(Long calendarId, UpdateAcademicCalenderRequest request) {
        AcademicCalendar calendar = calendarRepo.findById(calendarId).orElseThrow();
        calendar.setAcademicYear(request.getAcademicYear());
        calendar.setStartDate(request.getStartDate());
        calendar.setEndDate(request.getEndDate());
        calendarRepo.save(calendar);
        return mapToDto(calendar);
    }

    @Override
    public List<CalendarDto> getAcademicCalenders() {
        return calendarRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CalendarDto getAcademicCalender(Long calendarId) {
        return mapToDto(calendarRepo.findById(calendarId).orElseThrow());
    }

    @Override
    public HolidayDto updateHoliday(Long holidayId, Holiday holiday) {
        Holiday existing = holidayRepo.findById(holidayId).orElseThrow();
        existing.setHolidayName(holiday.getHolidayName());
        existing.setDescription(holiday.getDescription());
        existing.setHolidayDate(holiday.getHolidayDate());
        holidayRepo.save(existing);
        return mapHolidayToDto(existing);
    }

    @Override
    public String removeHoliday(Long holidayId) {
        holidayRepo.deleteById(holidayId);
        return "Holiday removed successfully";
    }

    @Override
    public String removeEvent(Long eventId) {
        eventRepo.deleteById(eventId);
        return "Event removed successfully";
    }

    @Override
    public EventDto updateEvent(Long eventId, Event event) {
        Event existing = eventRepo.findById(eventId).orElseThrow();
        existing.setEventName(event.getEventName());
        existing.setDescription(event.getDescription());
        existing.setEventDate(event.getEventDate());
        eventRepo.save(existing);
        return mapEventToDto(existing);
    }

    @Override
    public EventDto viewEvent(Long eventId) {
        return mapEventToDto(eventRepo.findById(eventId).orElseThrow());
    }

    @Override
    public HolidayDto viewHoliday(Long holidayId) {
        return mapHolidayToDto(holidayRepo.findById(holidayId).orElseThrow());
    }

    @Override
    public List<HolidayDto> viewHolidays() {
        return holidayRepo.findAll().stream().map(this::mapHolidayToDto).collect(Collectors.toList());
    }

    private CalendarDto mapToDto(AcademicCalendar cal) {
        return CalendarDto.builder()
                .id(cal.getId())
                .academicYear(cal.getAcademicYear())
                .startDate(cal.getStartDate())
                .endDate(cal.getEndDate())
                .createdBy(cal.getCreatedBy())
                .holidays(cal.getHolidays() == null ? null :
                        cal.getHolidays().stream().map(this::mapHolidayToDto).collect(Collectors.toList()))
                .events(cal.getEvents() == null ? null :
                        cal.getEvents().stream().map(this::mapEventToDto).collect(Collectors.toList()))
                .build();
    }

    private EventDto mapEventToDto(Event e) {
        return EventDto.builder()
                .id(e.getId())
                .eventName(e.getEventName())
                .description(e.getDescription())
                .eventDate(e.getEventDate())
                .build();
    }

    private HolidayDto mapHolidayToDto(Holiday h) {
        return HolidayDto.builder()
                .id(h.getId())
                .holidayName(h.getHolidayName())
                .holidayDate(h.getHolidayDate())
                .description(h.getDescription())
                .build();
    }

    @Override
    public AcademicCalendar createAdminAcademicCalendar(AcademicCalendar calendar){
        try {
            if (calendar.getHolidays() != null) {
                for (Holiday h : calendar.getHolidays()) {
                    h.setCalendar(calendar);
                }
            }
            if (calendar.getEvents() != null) {
                for (Event e : calendar.getEvents()) {
                    e.setCalendar(calendar);
                }
            }
            return calendarRepo.save(calendar);
        }catch(RuntimeException e){
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public List<AcademicCalendar> getAdminAcademicCalendar(){
        return calendarRepo.findAll();
    }

    @Override
    public AcademicCalendar getAcademicCalendarById(Long id) {
        Optional<AcademicCalendar> a1 = calendarRepo.findById(id);
        if (a1.isPresent()) {
            return a1.get();
        }
        throw new ResourceNotFoundException("Cannot find the calendar with the given ID");
    }

    @Override
    public List<Holiday> getAdminHoldiay(){
        return holidayRepo.findAll();
    }

    @Override
    public Holiday getAdminHolidayById(Long id) throws ResourceNotFoundException {
        Optional<Holiday> holiday1=holidayRepo.findById(id);
        if(holiday1.isPresent()){
            return holiday1.get();
        }
        throw new ResourceNotFoundException("Cannot find the holiday with the given ID");
    }

    @Override
    public Holiday addAdminHoliday(Holiday holiday){
      try{
          return holidayRepo.save(holiday);
      }catch(RuntimeException e){
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Holiday updateAdminHoliday(Long id,HolidayDto holiday) throws ResourceNotFoundException{
        Optional<Holiday> holiday1=holidayRepo.findById(id);
        if(holiday1.isPresent()){
            Holiday h1=holiday1.get();
            h1.setDescription(holiday.getDescription());
            h1.setHolidayDate(holiday.getHolidayDate());
            h1.setHolidayName(holiday.getHolidayName());
            holidayRepo.save(h1);
            return h1;
        }
        throw new ResourceNotFoundException("Cannot find the holiday with the given ID");
    }

    @Override
    public String deleteAdminHoliday(Long id) throws ResourceNotFoundException {
        Optional<Holiday> holiday1=holidayRepo.findById(id);
        if(holiday1.isPresent()) {
            com.DigitalClassRoomManagement.Entity.AcademicCalendar ac = holiday1.get().getCalendar();
            calendarRepo.deleteById(ac.getId());
            holidayRepo.deleteById(id);
            return "Holiday Deleted SuccessFully";
        }
        throw new ResourceNotFoundException("Cannot find the holiday with the given ID");
    }

    @Override
    public List<Event> getAdminEvent() {
        return eventRepo.findAll();
    }

    @Override
    public Event getAdminEventById(Long id) throws ResourceNotFoundException {
        return eventRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find the event with the given ID"));
    }

    @Override
    public Event addAdminEvent(Event event) {
        try {
            return eventRepo.save(event);
        }catch(RuntimeException e){
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Event updateAdminEvent(Long id, EventDto eventDto) throws ResourceNotFoundException {
        Event existing = eventRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find the event with the given ID"));

        existing.setEventName(eventDto.getEventName());
        existing.setDescription(eventDto.getDescription());
        existing.setEventDate(eventDto.getEventDate());

        return eventRepo.save(existing);
    }

    @Override
    public String deleteAdminEvent(Long id) throws ResourceNotFoundException {
        Event event = eventRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find the event with the given ID"));

        eventRepo.delete(event);


        return "Event deleted successfully";
    }

    @Override
    public  List<Holiday> viewStudentCalendarHoliday(){
        return holidayRepo.findAll();
    }

    @Override
    public List<Event> viewStudentCalendarEvents(){
        return eventRepo.findAll();
    }
    @Override
    public  List<Holiday> viewTeacherCalendarHoliday(){
        return holidayRepo.findAll();
    }

    @Override
    public List<Event> viewTeacherCalendarEvents(){
        return eventRepo.findAll();
    }



    @Scheduled(cron = "0 0 9 * * ?")
    @Override
    public void sendHolidayReminder() {

        LocalDate reminderDate = LocalDate.now().plusDays(2);

        List<Holiday> holidays =
                holidayRepo.findByHolidayDate(reminderDate);

        for (Holiday h : holidays) {

            KafkaNotificationDto dto = KafkaNotificationDto.builder()
                    .title(" Upcoming Holiday")
                    .message("Reminder: " + h.getHolidayName()
                            + " on " + h.getHolidayDate())
                    .source("HOLIDAY")
                    .build();

            kafkaTemplate.send("notification-topic", dto);
        }
    }
}
