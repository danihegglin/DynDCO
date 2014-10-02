(function() {
  define(["knockout", "map", "gps", "mockGps"], function(ko, Map, Gps, MockGps) {
    var MainPageModel;
    MainPageModel = (function() {
      function MainPageModel() {
        this.email = ko.observable();
        this.connecting = ko.observable();
        this.disconnected = ko.observable(true);
        this.mockGps = ko.observable();
        this.gps = ko.observable();
        this.closing = false;
        if (localStorage.email) {
          this.email(localStorage.email);
          this.connect();
        }
      }

      MainPageModel.prototype.submitEmail = function() {
        localStorage.email = this.email();
        return this.connect();
      };

      MainPageModel.prototype.connect = function() {
        var email;
        email = this.email();
        this.connecting("Connecting...");
        this.disconnected(null);
        this.ws = new WebSocket($("meta[name='websocketurl']").attr("content") + email);
        this.ws.onopen = (function(_this) {
          return function(event) {
            _this.connecting(null);
            _this.map = new Map(_this.ws);
            return _this.gps(new Gps(_this.ws));
          };
        })(this);
        this.ws.onclose = (function(_this) {
          return function(event) {
            if (!event.wasClean && !self.closing) {
              _this.connect();
              _this.connecting("Reconnecting...");
            } else {
              _this.disconnected(true);
            }
            _this.closing = false;
            if (_this.map) {
              _this.map.destroy();
            }
            if (_this.mockGps()) {
              _this.mockGps().destroy();
            }
            if (_this.gps()) {
              _this.gps().destroy();
            }
            _this.map = null;
            _this.mockGps(null);
            return _this.gps(null);
          };
        })(this);
        return this.ws.onmessage = (function(_this) {
          return function(event) {
            var json;
            json = JSON.parse(event.data);
            if (json.event === "user-positions") {
              return _this.map.updateMarkers(json.positions.features);
            }
          };
        })(this);
      };

      MainPageModel.prototype.disconnect = function() {
        this.closing = true;
        return this.ws.close();
      };

      MainPageModel.prototype.toggleMockGps = function() {
        if (this.mockGps()) {
          this.mockGps().destroy();
          this.mockGps(null);
          return this.gps(new Gps(this.ws));
        } else {
          if (this.gps()) {
            this.gps().destroy();
          }
          this.gps(null);
          return this.mockGps(new MockGps(this.ws));
        }
      };

      return MainPageModel;

    })();
    return MainPageModel;
  });

}).call(this);

//# sourceMappingURL=mainPage.js.map
