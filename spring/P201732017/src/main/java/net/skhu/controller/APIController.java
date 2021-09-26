package net.skhu.controller;

import net.skhu.domain.Category;
import net.skhu.domain.Dealer;
import net.skhu.domain.Product;
import net.skhu.domain.Supply;
import net.skhu.repository.CategoryRepository;
import net.skhu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping(value="/api")
public class APIController {

    @Autowired ProductRepository productRepository;

    @Autowired CategoryRepository categoryRepository;

    @RequestMapping(value = "products", method = RequestMethod.GET)
    public List<Product> products(){
        return productRepository.findAll();
    }

    @RequestMapping(value = "product/{id}", method = RequestMethod.GET)
    public Product findByIdProduct(@PathVariable("id") int id){
        return productRepository.findById(id).get();
    }

    @RequestMapping(value = "category/{id}", method = RequestMethod.GET)
    public Category findByIdCategory(@PathVariable("id") int id){
        return categoryRepository.findById(id).get();
    }

    @RequestMapping(value = "category/{id}/products", method = RequestMethod.GET)
    public List<Product> categoryJoinProduct(@PathVariable("id") int id){
        return categoryRepository.findById(id).get().getProducts();
    }

    @RequestMapping(value = "product/{id}/supplies", method = RequestMethod.GET)
    public List<Supply> productJoinSupply(@PathVariable("id") int id){
        return productRepository.findById(id).get().getSupplies();
    }


    @RequestMapping(value = "product/{id}/dealers/v1", method = RequestMethod.GET)
    public List<Dealer> productJoinSupplyJoinDealer1(@PathVariable("id") int id){
        List<Dealer> dealers = new ArrayList<>();
        List<Supply> supplies = productRepository.findById(id).get().getSupplies();
        for(Supply s : supplies)
            dealers.add(s.getDealer());
        return dealers;
    }


    @RequestMapping(value = "product/{id}/dealers/v2", method = RequestMethod.GET)
    public Stream<Dealer> productJoinSupplyJoinDealer2(@PathVariable("id") int id){
        return productRepository.findById(id).get()
                .getSupplies()
                .stream()
                .map(s -> s.getDealer());
    }

}
