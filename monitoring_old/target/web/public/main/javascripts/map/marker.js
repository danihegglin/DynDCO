(function() {
  define(["leaflet", "markerRenderer"], function(Leaflet, renderer) {
    var Marker;
    Marker = (function() {
      function Marker(map, feature, latLng) {
        var userId;
        this.map = map;
        this.feature = feature;
        if (feature.properties.count) {
          this.marker = new Leaflet.Marker(latLng, {
            icon: renderer.createClusterMarkerIcon(feature.properties.count)
          });
        } else {
          userId = feature.id;
          this.marker = new Leaflet.Marker(latLng, {
            title: feature.id
          });
          this.marker.bindPopup(renderer.renderPopup(userId));
        }
        this.lastSeen = new Date().getTime();
        this.marker.addTo(map);
      }

      Marker.prototype.update = function(feature, latLng) {
        var lastUpdate, time, updated;
        this.marker.setLatLng(latLng);
        if (feature.properties.count) {
          if (feature.properties.count !== this.feature.properties.count) {
            this.marker.setIcon(renderer.createClusterMarkerIcon(feature.properties.count));
          }
        }
        lastUpdate = this.feature.properties.timestamp;
        updated = feature.properties.timestamp;
        time = updated - lastUpdate;
        if (time > 0) {
          if (time > 10000) {
            time = 10000;
          }
          renderer.transition(this.marker._icon, time);
          if (this.marker._shadow) {
            renderer.transition(this.marker._shadow, time);
          }
        }
        this.feature = feature;
        return this.lastSeen = new Date().getTime();
      };

      Marker.prototype.snap = function() {
        renderer.resetTransition(this.marker._icon);
        if (this.marker._shadow) {
          return renderer.resetTransition(this.marker._shadow);
        }
      };

      Marker.prototype.remove = function() {
        return this.map.removeLayer(this.marker);
      };

      return Marker;

    })();
    return Marker;
  });

}).call(this);

//# sourceMappingURL=marker.js.map
