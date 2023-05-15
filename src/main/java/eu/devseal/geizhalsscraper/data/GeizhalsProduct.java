package eu.devseal.geizhalsscraper.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeizhalsProduct {
    private int offerID;
    private double unitPrice;
    private List<Double> shippingCost;
    private String company;
}
