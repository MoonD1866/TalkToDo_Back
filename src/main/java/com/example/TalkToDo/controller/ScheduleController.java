package com.example.TalkToDo.controller;

import com.example.TalkToDo.dto.ScheduleDTO;
import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.service.ScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // 모든 일정 조회
    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    // 특정 일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    // 일정 생성
    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.createSchedule(scheduleDTO));
    }

    // 일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, scheduleDTO));
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }

    // 사용자의 모든 일정 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduleDTO>> getAllSchedulesByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(scheduleService.getAllSchedulesByUserId(Long.parseLong(userId)));
    }

    // 기간별 일정 조회
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByDateRange(
            @PathVariable String userId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(scheduleService.getSchedulesByDateRange(Long.parseLong(userId), start, end));
    }

    // 카테고리별 일정 조회
    @GetMapping("/user/{userId}/category")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByCategory(
            @PathVariable String userId,
            @RequestParam String category) {
        return ResponseEntity.ok(scheduleService.getSchedulesByCategory(Long.parseLong(userId), category));
    }

    // 할일 목록 조회
    @GetMapping("/user/{userId}/todos")
    public ResponseEntity<List<ScheduleDTO>> getTodosByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(scheduleService.getTodosByUserId(Long.parseLong(userId)));
    }

    // 날짜별 일정 조회 (통합된 메서드)
    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByDate(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.getSchedulesByDate(userId, date));
    }

    @PostMapping("/{scheduleId}/add-to-my-schedule")
    public ResponseEntity<?> addToMySchedule(@PathVariable Long scheduleId, @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        scheduleService.addScheduleToUser(scheduleId, Long.parseLong(user.getUsername()));
        return ResponseEntity.ok().build();
    }

    // 월별 일정 조회
    @GetMapping("/user/{userId}/month/{year}/{month}")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByMonth(
            @PathVariable Long userId,
            @PathVariable int year,
            @PathVariable int month) {
        List<Schedule> schedules = scheduleService.getSchedulesByMonth(userId, year, month);
        List<ScheduleDTO> scheduleDTOs = schedules.stream()
                .map(scheduleService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(scheduleDTOs);
    }

    // 할일을 캘린더에 추가
    @PostMapping("/todo/{todoId}/calendar")
    public ResponseEntity<ScheduleDTO> addTodoToCalendar(
            @PathVariable Long todoId,
            @RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleService.convertToEntity(scheduleDTO);
        Schedule savedSchedule = scheduleService.addTodoToCalendar(todoId, schedule);
        return ResponseEntity.ok(scheduleService.convertToDTO(savedSchedule));
    }

    // 할일을 캘린더에서 제거
    @DeleteMapping("/todo/{todoId}/calendar")
    public ResponseEntity<?> removeTodoFromCalendar(@PathVariable Long todoId) {
        scheduleService.removeTodoFromCalendar(todoId);
        return ResponseEntity.ok().build();
    }
} 