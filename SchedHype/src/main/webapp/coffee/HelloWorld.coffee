class ViewModel
    constructor: (first, last) ->
        @firstName = ko.observable(first)
        @lastName =  ko.observable(last)
        @fullName = ko.computed =>
            @firstName() + " " + @lastName()

$ ->
    ko.applyBindings(new ViewModel("CoffeeScript", "Fan"))