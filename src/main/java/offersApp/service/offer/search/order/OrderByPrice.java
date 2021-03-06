package offersApp.service.offer.search.order;

import offersApp.entity.Offer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderByPrice implements Ordering {
    public List<Offer> order(List<Offer> offers){
        offers.sort((o1, o2) -> {
           if (o1.getPrice() < o2.getPrice()) return -1;
           if (o1.getPrice() > o2.getPrice()) return 1;
           return 0;
        });
        return offers;
    }
}
