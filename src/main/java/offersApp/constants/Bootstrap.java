package offersApp.constants;

import offersApp.dto.*;
import offersApp.dto.builder.OfferDtoBuilder;
import offersApp.entity.*;
import offersApp.repository.RoleRepository;
import offersApp.service.category.CategoryService;
import offersApp.service.offer.OfferService;
import offersApp.service.review.ReviewService;
import offersApp.service.sale.LimittedStockException;
import offersApp.service.sale.SaleService;
import offersApp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static offersApp.constants.ApplicationConstants.Categories.*;
import static offersApp.constants.ApplicationConstants.Roles.*;

@Component
public class Bootstrap {
    private RoleRepository roleRepository;
    private UserService userService;
    private OfferService offerService;
    private CategoryService categoryService;
    private SaleService saleService;
    private ReviewService reviewService;

    @Autowired
    public Bootstrap(RoleRepository roleRepository, UserService userService, OfferService offerService, CategoryService categoryService, SaleService saleService, ReviewService reviewService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.offerService = offerService;
        this.categoryService = categoryService;
        this.saleService = saleService;
        this.reviewService = reviewService;
    }

    @PostConstruct
    public void populateDb(){
        addRoles();
        addCategories();
        addAdmin();
        addAgents();
        addCustomers();
        addOffers();
        addSales();
        addReviews();

        // test update category
        // updateCategoryAfterAll();

    }

    private void addRoles(){
        Role adminRole = new Role(ADMINISTRATOR);
        Role customerRole = new Role(CUSTOMER);
        Role agentRole = new Role(AGENT);

        roleRepository.save(adminRole);
        roleRepository.save(customerRole);
        roleRepository.save(agentRole);
    }

    private void addCategories(){
     for(String categoryName: CATEGORIES){
         CategoryDto categoryDto = new CategoryDto(categoryName);
         categoryService.create(categoryDto);
     }
    }

    private void addAdmin(){
        UserDto admin = new UserDto();
        admin.setUsername("administrator@yahoo.com");
        admin.setPassword("Administrator1#");
        admin.setRole(ADMINISTRATOR);
        admin.setEmail("anc.faur@gmail.com");
        userService.register(admin);
    }

    private void addAgents(){
        UserDto agent1 = new UserDto();
        agent1.setUsername("agent1@yahoo.com");
        agent1.setPassword("Agent1#");
        agent1.setRole(AGENT);
        agent1.setEmail("anc.faur@gmail.com");
        userService.register(agent1);

        UserDto agent2 = new UserDto();
        agent2.setUsername("agent2@yahoo.com");
        agent2.setPassword("Agent1#");
        agent2.setRole(AGENT);
        agent2.setEmail("anc.faur@gmail.com");
        userService.register(agent2);

    }

    private void addCustomers(){
        UserDto customer1 = new UserDto();
        customer1.setUsername("customer1@yahoo.com");
        customer1.setPassword("Customer1#");
        customer1.setRole(CUSTOMER);
        customer1.setEmail("anc.faur@gmail.com");
        UserDto back1 =userService.register(customer1);


        List<String> categoryNames1= new ArrayList<>();
        categoryNames1.add(CULINAR);
        categoryNames1.add(FAMILY);
        for (String categoryName: categoryNames1){
            categoryService.subscribeUser(categoryName, back1.getId());
        }


        UserDto customer2 = new UserDto();
        customer2.setUsername("customer2@yahoo.com");
        customer2.setPassword("Customer1#");
        customer2.setRole(CUSTOMER);
        customer2.setEmail("anc.faur@gmail.com");
        UserDto back2 =userService.register(customer2);

        List<String> categoryNames2= new ArrayList<>();
        categoryNames2.add(ROMANTIC);
        categoryNames2.add(NIGHTLIFE);
        for (String categoryName: categoryNames2){
            categoryService.subscribeUser(categoryName, back2.getId());
        }

        // test unsubscribe
        //categoryService.unsubscribeUser(ROMANTIC, back2.getId());
        //categoryService.unsubscribeUser(NIGHTLIFE, back2.getId());

    }

    private void addOffers() {
        DiscountDto discountDto1 =  new DiscountDto(5, 20);

        List<String> categoryNames1 = new ArrayList<>();
        categoryNames1.add(ROMANTIC);
        categoryNames1.add(PHYSICAL);

        OfferDto offerDto1 =new OfferDtoBuilder()
                .setName("Caluti")
                .setPrice(50)
                .setInStock(20)
                .setLocation("Padurea Baciu")
                .setDescription("E fain sa calaresti cu noi")
                .setAgent(new Long(2))
                .setImage(null)
                .setNoPersons(1)
                .setDatePublished(new Date())
                .setCategories(categoryNames1)
                .setInitialNo(40)
                .build();
        offerService.create(offerDto1, discountDto1);


        DiscountDto discountDto2 =  new DiscountDto(2, 10);

        List<String> categoryNames2 = new ArrayList<>();
        categoryNames2.add(ROMANTIC);
        categoryNames2.add(ACCOMODATION);

        OfferDto offerDto2 =new OfferDtoBuilder()
                .setName("Weekend Paris")
                .setPrice(200)
                .setInStock(10)
                .setLocation("Chiar langa Turnul Eiffel")
                .setDescription("Hotel 4 stele, 3 nopti, in weekend")
                .setAgent(new Long(3))
                .setImage(null)
                .setNoPersons(2)
                .setDatePublished(new Date())
                .setCategories(categoryNames2)
                .setInitialNo(10)
                .build();
        OfferDto back =offerService.create(offerDto2, discountDto2);

        // test delete
        // offerService.delete(back.getId());


        //test update
//        List<String> allCategories = back.getCategories();
//        allCategories.add(FAMILY);
//        back.setCategories(allCategories);
//        DiscountDto discountDto22 =  new DiscountDto(155, 20);
//
//        offerService.update(back, discountDto22);

    }

    private void updateCategoryAfterAll(){
        CategoryDto categoryDto = new CategoryDto(10L, "romantic");
        categoryService.update(categoryDto);
    }

    private void addSales(){

        // alowed with no discount
        SaleDto saleDto1 = new SaleDto(4L, 1L, 1, new Date());
        try {
            System.out.print("*******Sale1 sum is ="+ saleService.sell(saleDto1) );
        } catch (LimittedStockException e) {
           System.out.print("*******Sale 1-> Should not occur");
        }

        // alowed with discount
        SaleDto saleDto2 = new SaleDto(4L, 1L, 5, new Date());
        try {
            System.out.print("*******Sale2 sum is ="+ saleService.sell(saleDto2) );
        } catch (LimittedStockException e) {
            System.out.print("*******Sale 2-> Should not occur");
        }

        // not allowed
        SaleDto saleDto3 = new SaleDto(4L, 1L, 100, new Date());
        try {
            saleService.sell(saleDto3);
            System.out.print("*******Sale3 ");
        } catch (LimittedStockException e) {
            System.out.print("*******Sale 3-> Limited stock is okay");
        }

    }

    private void addReviews(){
        ReviewDto reviewDto1 = new ReviewDto(1L, 4L, new Date(), 4, "A fost chiar ok");
        ReviewDto reviewDto2 = new ReviewDto(1L, 5L, new Date(), 5,"Genial!");
        ReviewDto reviewDto3 = new ReviewDto(2L, 5L, new Date(), 1,"Penal! Mai bine mergeam la scoala");

        ReviewDto back1 =reviewService.create(reviewDto1);
        ReviewDto back2 =reviewService.create(reviewDto2);
        reviewService.create(reviewDto3);


//        // test update
//        reviewDto1.setId(back1.getId());
//        reviewDto1.setDescription("A fost okay, dar se putea mai bine");
//        reviewService.update(reviewDto1);

//        // test delete
//        reviewService.delete(back2.getId());


    }
}
