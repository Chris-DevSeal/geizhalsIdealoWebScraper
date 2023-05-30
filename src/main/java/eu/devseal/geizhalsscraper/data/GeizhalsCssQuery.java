package eu.devseal.geizhalsscraper.data;

public enum GeizhalsCssQuery {
    PRODUCT_LISTINGS("div.row.offer"),
    SHIPPING_COST("div.offer__delivery div.offer__delivery-payment span.gh_extracost"),
    COMPANY("div.offer__merchant a"),
    COMPANY_ATTRIBUTE("data-merchant-name"),
    UNIT_PRICE("div.offer__price span");
    public final String url;

    GeizhalsCssQuery(String url) {
        this.url = url;
    }
    }
