package eu.devseal.geizhalsscraper.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String geizhalsURL;
    private String idealoURL;
    private double minPrice;
    private double maxPrice;
    //    LIRIKA_BLACK("https://geizhals.at/philips-saeco-lirika-10004476-a1075297.html"),
    //    LIRIKA_PLUS("https://geizhals.at/philips-saeco-lirika-macchiato-10004477-a1075335.html"),
    //    LIRIKA_OTC("https://geizhals.at/philips-saeco-lirika-one-touch-cappuccino-titan-10004768-a1548532.html"),
    //    ROYAL_BLACK("https://geizhals.at/saeco-royal-black-a2414719.html"),
    //    ROYAL_OTC("https://geizhals.at/saeco-new-royal-one-touch-cappucino-9j0080-a2465588.html"),
    //    EVO_BLACK("https://geizhals.at/philips-saeco-aulika-evo-black-10000045-a2171537.html"),
    //    EVO_FOCUS("https://geizhals.at/philips-saeco-aulika-evo-focus-10000040-a2171515.html"),
    //    EVO_OFFICE("https://geizhals.at/philips-saeco-aulika-evo-office-10000044-a2183014.html"),
    //    EVO_TOP("https://geizhals.at/saeco-aulika-evo-top-hsc-10005374-a2411516.html"),
    //    EVO_TOP_RI("https://geizhals.at/saeco-aulika-evo-top-hsc-ri-10005373-a2411511.html");

}
