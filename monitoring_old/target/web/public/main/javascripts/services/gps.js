(function() {
  define(function() {
    var Gps;
    Gps = (function() {
      function Gps(ws) {
        this.ws = ws;
        this.lastSent = 0;
        this.lastPosition = null;
        this.intervalId = setInterval((function(_this) {
          return function() {
            if (_this.lastPosition) {
              return _this.sendPosition(_this.lastPosition);
            }
          };
        })(this), 10000);
        this.watchId = navigator.geolocation.watchPosition((function(_this) {
          return function(position) {
            return _this.sendPosition(position);
          };
        })(this));
      }

      Gps.prototype.sendPosition = function(position) {
        var time;
        this.lastPosition = position;
        time = new Date().getTime();
        if (time - this.lastSent > 2000) {
          this.lastSent = time;
          return this.ws.send(JSON.stringify({
            event: "user-moved",
            position: {
              type: "Point",
              coordinates: [position.coords.longitude, position.coords.latitude]
            }
          }));
        }
      };

      Gps.prototype.destroy = function() {
        navigator.geolocation.clearWatch(this.watchId);
        return clearInterval(this.intervalId);
      };

      return Gps;

    })();
    return Gps;
  });

}).call(this);

//# sourceMappingURL=gps.js.map
