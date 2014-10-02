(function() {
  define(function() {
    return {
      lastArea: function() {
        var e, lastArea;
        if (localStorage.lastArea) {
          try {
            lastArea = JSON.parse(localStorage.lastArea);
            return lastArea;
          } catch (_error) {
            e = _error;
            return localStorage.removeItem("lastArea");
          }
        }
      },
      setLastArea: function(lastArea) {
        return localStorage.lastArea = JSON.stringify(lastArea);
      }
    };
  });

}).call(this);

//# sourceMappingURL=storage.js.map
