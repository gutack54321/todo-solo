package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodoService service;

    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO dto){
        try{
//            String temporaryUserId =  "temporary-user"; //로그인 기능이 없어서 임시 유저아이디

            TodoEntity entity = TodoDTO.toEntity(dto); //dto를 엔티티로 변환

            entity.setId(null);  //생성이기에 엔티티에 id가 없어야 됨. dto에서 받은 id로 넣어놓으면 안됨.

            entity.setUserId(userId);
            log.info("title1 {}", entity.getTitle());

            List<TodoEntity> entities = service.create(entity); //서비스 로직을 이용하여 엔티티 리스트 추출

            log.info("title2 {}", entities.get(0));

//            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList()); //엔티티 리스트를 dto 리스트로 변환

            List<TodoDTO> dtos = new ArrayList<>();
            for(TodoEntity todoEntity: entities){
                TodoDTO todoDTO = TodoEntity.toDTO(todoEntity);
                dtos.add(todoDTO);
            }


            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();


            return ResponseEntity.ok().body(response);
            }
        catch (Exception e){

            // 예외 발생 시 ResponseDTO에 error에 메시지를 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){
//        String temporaryUserId =  "temporary-user";

        List<TodoEntity> entities = service.retrieve(userId);

//        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        List<TodoDTO> dtos = new ArrayList<>();
        for(TodoEntity todoEntity: entities){
            TodoDTO todoDTO = TodoEntity.toDTO(todoEntity);
            dtos.add(todoDTO);
        }

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO dto){
        try{
//            String temporaryUserId =  "temporary-user"; //로그인 기능이 없어서 임시 유저아이디

            TodoEntity entity = TodoDTO.toEntity(dto); //dto를 엔티티로 변환

            entity.setUserId(userId);

            List<TodoEntity> entities = service.update(entity); //서비스 로직을 이용하여 엔티티 리스트 추출

//            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList()); //엔티티 리스트를 dto 리스트로 변환

            List<TodoDTO> dtos = new ArrayList<>();
            for(TodoEntity todoEntity: entities){
                TodoDTO todoDTO = TodoEntity.toDTO(todoEntity);
                dtos.add(todoDTO);
            }

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            // 예외 발생 시 ResponseDTO에 error에 메시지를 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO dto){
        try{
//            String temporaryUserId =  "temporary-user"; //로그인 기능이 없어서 임시 유저아이디

            TodoEntity entity = TodoDTO.toEntity(dto); //dto를 엔티티로 변환

            entity.setUserId(userId);

            List<TodoEntity> entities = service.delete(entity); //서비스 로직을 이용하여 엔티티 리스트 추출

//            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList()); //엔티티 리스트를 dto 리스트로 변환

            List<TodoDTO> dtos = new ArrayList<>();
            for(TodoEntity todoEntity: entities){
                TodoDTO todoDTO = TodoEntity.toDTO(todoEntity);
                dtos.add(todoDTO);
            }

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            // 예외 발생 시 ResponseDTO에 error에 메시지를 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
