package com.tutorial.userservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tutorial.userservice.entity.Usuario;
import com.tutorial.userservice.feignclients.BikeFeignClient;
import com.tutorial.userservice.feignclients.CarFeignClient;
import com.tutorial.userservice.model.Bike;
import com.tutorial.userservice.model.Car;
import com.tutorial.userservice.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CarFeignClient carFeignClient;
    @Autowired
    BikeFeignClient bikeFeignClient;

    public List<Usuario> getAll(){
        return userRepository.findAll();
    }

    public Usuario getUserById(int id){
        return userRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario user){
        Usuario userNew = userRepository.save(user);
        return userNew;
    }
    @SuppressWarnings("unchecked")
    public List<Car> getCars(int userId){
        List<Car> cars = restTemplate.getForObject("http://localhost:8002/car/byuser/"+userId,List.class);
        return cars;
    }
    @SuppressWarnings("unchecked")
    public List<Bike> getBikes(int userId){
        List<Bike> bike = restTemplate.getForObject("http://localhost:8003/bike/byuser/"+userId, List.class);
        return bike;
    }

    public Car save(int userId, Car car){
        car.setUserId(userId);
        Car carNew = carFeignClient.save(car);
        return carNew;
    }

    public Bike save(int userId, Bike bike){
        bike.setUserId(userId);
        Bike bikeNew = bikeFeignClient.save(bike);
        return bikeNew;
    }

    public Map<String,Object> getUserAndVehicule(int userId){
        Map<String,Object> result = new HashMap<>();
        Usuario user = userRepository.findById(userId).orElse(null);
        if(user == null){
            result.put("Mensaje", "Usuario no existe");
            return result;
        }else{    
            result.put("Usuario", user);
        }
        List<Car> car = carFeignClient.getCars(userId);
        if(car.isEmpty()){
            result.put("Autos", "Usuario no tiene Autos");
        }else{
            result.put("Autos", car);
        }
        List<Bike> bike = bikeFeignClient.getBikes(userId);
        if(bike.isEmpty()){
            result.put("Motos", "Usuario no tiene Motos");
        }else{
            result.put("Motos", bike);
        }   
        return result;
    }
}
