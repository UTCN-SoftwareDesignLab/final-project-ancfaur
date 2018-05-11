package offersApp.service.offer;

import offersApp.converter.offer.DiscountConverter;
import offersApp.converter.offer.OfferConverter;
import offersApp.dto.DiscountDto;
import offersApp.dto.OfferDto;
import offersApp.entity.Category;
import offersApp.entity.Discount;
import offersApp.entity.Offer;
import offersApp.entity.User;
import offersApp.repository.CategoryRepository;
import offersApp.repository.DiscountRepository;
import offersApp.repository.OfferRepository;
import offersApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;
    private DiscountRepository discountRepository;
    private DiscountConverter discountConverter;
    private OfferConverter offerConverter;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, DiscountRepository discountRepository, DiscountConverter discountConverter, OfferConverter offerConverter, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.offerRepository = offerRepository;
        this.discountRepository = discountRepository;
        this.discountConverter = discountConverter;
        this.offerConverter = offerConverter;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public OfferDto create(OfferDto offerDto, DiscountDto discountDto) {
        User agent = userRepository.findById(offerDto.getAgentId()).orElse(null);
        Discount discount = discountConverter.fromDto(discountDto);

        List<Category> categories = new ArrayList<>();

        for(String categoryName:offerDto.getCategories()){
            categories.add(categoryRepository.findByName(categoryName));
        }

        Offer offer = offerConverter.fromDto(offerDto, categories, agent, discount);
        Offer back =offerRepository.save(offer);
        offerDto.setId(back.getId());
        return offerDto;
    }

    @Override
    public void delete(Long offerId) {
        Offer offer = offerRepository.findById(offerId).orElse(null);
        offerRepository.delete(offer);
    }

    @Override
    public void update(OfferDto offerDto, DiscountDto discountDto) {
        delete(offerDto.getId());
        create(offerDto, discountDto);
    }

    @Override
    public OfferDto findById(Long offerId) {
        return offerConverter.toDto(offerRepository.findById(offerId).orElse(null));

    }
}
