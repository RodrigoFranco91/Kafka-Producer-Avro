package br.com.springkafka.controller;

import br.com.springkafka.People;
import br.com.springkafka.dto.PeopleDto;
import br.com.springkafka.producer.PeopleProducer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Collectors;


@RestController()
@RequestMapping("/peoples")
@AllArgsConstructor
public class PeopleController {

    private  PeopleProducer peopleProducer;

    @PostMapping
    public ResponseEntity sendMessage(@RequestBody PeopleDto peopleDto){

        var id = UUID.randomUUID().toString();

        //Criando o People de Avro
        var message = People.newBuilder()
                .setId(id)
                .setName(peopleDto.getName())
                .setCpf(peopleDto.getCpf())
                .setBooks(peopleDto.getBooks().stream().map(p -> (CharSequence) p).collect(Collectors.toList()))
                .build();

        peopleProducer.sendMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
