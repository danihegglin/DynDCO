(function() {
  define(["leaflet"], function(Leaflet) {
    var MockGps;
    MockGps = (function() {
      function MockGps(ws) {
        var e, position;
        this.ws = ws;
        this.map = Leaflet.map("mockGps");
        new Leaflet.TileLayer("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
          minZoom: 1,
          maxZoom: 16,
          attribution: "Map data © OpenStreetMap contributors"
        }).addTo(this.map);
        position;
        if (localStorage.lastGps) {
          try {
            position = JSON.parse(localStorage.lastGps);
          } catch (_error) {
            e = _error;
            localStorage.removeItem("lastGps");
            position = [0, 0];
          }
        } else {
          position = [0, 0];
        }
        this.map.setView(position, 4);
        this.marker = new Leaflet.Marker(position, {
          draggable: true
        }).addTo(this.map);
        this.marker.on("dragend", (function(_this) {
          return function() {
            return _this.sendPosition();
          };
        })(this));
        this.sendPosition();
      }

      MockGps.prototype.sendPosition = function() {
        var position;
        position = this.marker.getLatLng();
        localStorage.lastGps = JSON.stringify(position);
        return this.ws.send(JSON.stringify({
          event: "user-moved",
          position: {
            type: "Point",
            coordinates: [position.lng, position.lat]
          }
        }));
      };

      MockGps.prototype.destroy = function() {
        var e;
        try {
          return this.map.remove();
        } catch (_error) {
          e = _error;
        }
      };

      return MockGps;

    })();
    return MockGps;
  });

}).call(this);

//# sourceMappingURL=mockGps.js.map
