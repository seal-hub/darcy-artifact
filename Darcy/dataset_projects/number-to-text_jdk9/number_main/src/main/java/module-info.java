module number_main {
    requires number_api;

    exports no.dervis.numbertotext.generator;

    uses no.dervis.numbertotext.api.spi.NumberResourcesProvider;
}
