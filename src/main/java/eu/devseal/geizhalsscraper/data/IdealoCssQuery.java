package eu.devseal.geizhalsscraper.data;

public enum IdealoCssQuery {
    PRODUCT_LISTINGS("div#offer-list-with-pagination ul li.productOffers-listItem.row"),
    PRODUCT_PRICE("div.productOffers-listItemOfferShippingDetails"),
    PRODUCT_ATTRIBUTES("div.productOffers-listItemOffer div.row div.column div.amazon-prime__wrapper a.productOffers-listItemOfferPrice"),
    DATA_GTM_PAYLOAD("data-gtm-payload");

    public final String url;

    IdealoCssQuery(String url) {
        this.url = url;
    }
}
