(function() {
  define(["marker", "storage", "leaflet"], function(Marker, Storage, Leaflet) {
    var Map;
    Map = (function() {
      function Map(ws) {
        var e, lastArea;
        this.map = Leaflet.map("map");
        new Leaflet.TileLayer("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
          minZoom: 1,
          maxZoom: 16,
          attribution: "Map data © OpenStreetMap contributors"
        }).addTo(this.map);
        lastArea = Storage.lastArea();
        if (lastArea) {
          try {
            this.map.setView(lastArea.center, lastArea.zoom);
          } catch (_error) {
            e = _error;
            this.map.setView([0, 0], 2);
          }
        } else {
          this.map.setView([0, 0], 2);
        }
        this.ws = ws;
        this.markers = {};
        this.preZoomMarkers = {};
        this.sendArea = null;
        this.map.on("zoomstart", (function(_this) {
          return function() {
            return _this.snapMarkers();
          };
        })(this));
        this.map.on("zoomend", (function(_this) {
          return function() {
            var id;
            _this.snapMarkers();
            for (id in _this.markers) {
              _this.preZoomMarkers[id] = _this.markers[id];
            }
            _this.markers = {};
            return _this.updatePosition();
          };
        })(this));
        this.map.on("moveend", (function(_this) {
          return function() {
            return _this.updatePosition();
          };
        })(this));
        this.intervalId = setInterval((function(_this) {
          return function() {
            var id, marker, time, _results;
            time = new Date().getTime();
            _results = [];
            for (id in _this.markers) {
              marker = _this.markers[id];
              if (time - marker.lastSeen > 20000) {
                delete _this.markers[id];
                _results.push(marker.remove());
              } else {
                _results.push(void 0);
              }
            }
            return _results;
          };
        })(this), 5000);
        this.updatePosition();
      }

      Map.prototype.updatePosition = function() {
        if (this.sendArea) {
          clearTimeout(this.sendArea);
        }
        return this.sendArea = setTimeout((function(_this) {
          return function() {
            return _this.doUpdatePosition();
          };
        })(this), 500);
      };

      Map.prototype.doUpdatePosition = function() {
        var bounds, event;
        this.sendArea = null;
        bounds = this.map.getBounds();
        localStorage.lastArea = Storage.setLastArea({
          center: bounds.getCenter().wrap(-180, 180),
          zoom: this.map.getZoom()
        });
        event = {
          event: "viewing-area",
          area: {
            type: "Polygon",
            coordinates: [[[bounds.getSouthWest().lng, bounds.getSouthWest().lat], [bounds.getNorthWest().lng, bounds.getNorthWest().lat], [bounds.getNorthEast().lng, bounds.getNorthEast().lat], [bounds.getSouthEast().lng, bounds.getSouthEast().lat], [bounds.getSouthWest().lng, bounds.getSouthWest().lat]]],
            bbox: [bounds.getWest(), bounds.getSouth(), bounds.getEast(), bounds.getNorth()]
          }
        };
        return this.ws.send(JSON.stringify(event));
      };

      Map.prototype.updateMarkers = function(features) {
        var coordinates, feature, id, latLng, marker;
        for (id in features) {
          feature = features[id];
          marker = this.preZoomMarkers[feature.id] ? (marker = this.preZoomMarkers[feature.id], this.markers[feature.id] = marker, delete this.preZoomMarkers[feature.id], marker) : this.markers[feature.id];
          coordinates = feature.geometry.coordinates;
          latLng = this.wrapForMap(new Leaflet.LatLng(coordinates[1], coordinates[0]));
          if (marker) {
            marker.update(feature, latLng);
          } else {
            marker = new Marker(this.map, feature, latLng);
            this.markers[feature.id] = marker;
          }
        }
        for (id in this.preZoomMarkers) {
          this.preZoomMarkers[id].remove();
        }
        return this.preZoomMarkers = {};
      };

      Map.prototype.snapMarkers = function() {
        var id, _results;
        _results = [];
        for (id in this.markers) {
          _results.push(this.markers[id].snap());
        }
        return _results;
      };

      Map.prototype.destroy = function() {
        var e;
        try {
          this.map.remove();
          return clearInterval(this.intervalId);
        } catch (_error) {
          e = _error;
        }
      };

      Map.prototype.wrapForMap = function(latLng) {
        var center, offset;
        center = this.map.getBounds().getCenter();
        offset = center.lng - center.wrap(-180, 180).lng;
        if (offset !== 0) {
          return new Leaflet.LatLng(latLng.lat, latLng.lng + offset);
        } else {
          return latLng;
        }
      };

      return Map;

    })();
    return Map;
  });

}).call(this);

//# sourceMappingURL=map.js.map
