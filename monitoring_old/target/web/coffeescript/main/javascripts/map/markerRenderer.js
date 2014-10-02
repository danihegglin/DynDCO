(function() {
  define(["leaflet", "md5", "jquery"], function(Leaflet, md5) {
    var escapeHtml;
    escapeHtml = function(unsafe) {
      return unsafe.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
    };
    return {
      renderPopup: function(userId) {
        return "<p><img src='http://www.gravatar.com/avatar/" + md5(userId.toLowerCase()) + "'/></p><p>" + escapeHtml(userId) + "</p>";
      },
      createClusterMarkerIcon: function(count) {
        var className;
        className = count < 10 ? "cluster-marker-small" : count < 100 ? "cluster-marker-medium" : "cluster-marker-large";
        return new Leaflet.DivIcon({
          html: "<div><span>" + count + "</span></div>",
          className: "cluster-marker " + className,
          iconSize: new Leaflet.Point(40, 40)
        });
      },
      resetTransition: function(element) {
        var updateTransition;
        updateTransition = function(element, prefix) {
          return element.style[prefix + "transition"] = "";
        };
        updateTransition(element, "-webkit-");
        updateTransition(element, "-moz-");
        updateTransition(element, "-o-");
        return updateTransition(element, "");
      },
      transition: function(element, time) {
        var updateTransition;
        updateTransition = function(element, prefix) {
          return element.style[prefix + "transition"] = prefix + "transform " + time + "ms linear";
        };
        updateTransition(element, "-webkit-");
        updateTransition(element, "-moz-");
        updateTransition(element, "-o-");
        return updateTransition(element, "");
      }
    };
  });

}).call(this);

//# sourceMappingURL=markerRenderer.js.map
