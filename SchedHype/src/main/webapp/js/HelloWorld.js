// Generated by CoffeeScript 1.3.3
(function() {
  var ViewModel;

  ViewModel = (function() {

    function ViewModel(first, last) {
      var _this = this;
      this.firstName = ko.observable(first);
      this.lastName = ko.observable(last);
      this.fullName = ko.computed(function() {
        return _this.firstName() + " " + _this.lastName();
      });
    }

    return ViewModel;

  })();

  $(function() {
    return ko.applyBindings(new ViewModel("CoffeeScript", "Fan"));
  });

}).call(this);
