package com.tutorial.userservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.userservice.entity.Usuario;
import com.tutorial.userservice.model.Bike;
import com.tutorial.userservice.model.Car;
import com.tutorial.userservice.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        List<Usuario> users = userService.getAll();
        if (users.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable("id") int id){
        Usuario user = userService.getUserById(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario user){
        Usuario usernew = userService.save(user);
        return ResponseEntity.ok(usernew);
    }

    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable("userId") int userId){
        Usuario user = userService.getUserById(userId);
        if(user == null)
            return ResponseEntity.notFound().build();
        List<Car> cars = userService.getCars(userId);
        if(cars.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cars); 
    }

    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> getBikes(@PathVariable("userId") int userId){
        Usuario user = userService.getUserById(userId);
        if(user == null)
            return ResponseEntity.notFound().build();
        List<Bike> bikes = userService.getBikes(userId);
        if(bikes.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bikes); 
    }

    @PostMapping("/savecar/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable("userId") int userId, @RequestBody Car car){
        if(userService.getUserById(userId)==null)
            return ResponseEntity.notFound().build();
        Car newCar = userService.save(userId, car);
        return ResponseEntity.ok(newCar);
    } 

    @PostMapping("/savebike/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable("userId") int userId, @RequestBody Bike bike){
        if(userService.getUserById(userId)==null)
            return ResponseEntity.notFound().build();
        Bike newBike = userService.save(userId, bike);
        return ResponseEntity.ok(newBike);
    } 

    @GetMapping("/userandvehicules/{userId}")
    public ResponseEntity<Map<String,Object>> getUserAndVehicule(@PathVariable("userId") int userId){
        Map<String, Object> result = new HashMap<>();
        result = userService.getUserAndVehicule(userId);
        return ResponseEntity.ok(result);
    }
}
