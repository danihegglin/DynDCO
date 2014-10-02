(function() {
  require.config({
    paths: {
      mainPage: "./models/mainPage",
      map: "./map/map",
      marker: "./map/marker",
      markerRenderer: "./map/markerRenderer",
      gps: "./services/gps",
      mockGps: "./services/mockGps",
      storage: "./services/storage",
      md5: "./md5.min",
      bootstrap: "../lib/bootstrap/js/bootstrap",
      jquery: "../lib/jquery/jquery",
      knockout: "../lib/knockout/knockout",
      leaflet: "../lib/leaflet/leaflet"
    },
    shim: {
      bootstrap: {
        deps: ["jquery"],
        exports: "$"
      },
      jquery: {
        exports: "$"
      },
      knockout: {
        exports: "ko"
      }
    }
  });

  require(["knockout", "mainPage", "bootstrap"], function(ko, MainPageModel) {
    var model;
    model = new MainPageModel;
    return ko.applyBindings(model);
  });

}).call(this);

//# sourceMappingURL=main.js.map
