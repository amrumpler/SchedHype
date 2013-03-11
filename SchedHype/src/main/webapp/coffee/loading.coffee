(($) ->
    "use strict"
    methods =
        defaults:
            message: "Loading"
            id: ""
            visible: false
            type: "spinner"
            appendType: "prepend"
            beforeOpen: $.noop
            afterOpen: $.noop
            beforeClose: $.noop
            afterClose: $.noop
            color: "black"
            closeAll: true

        init: (options) ->
            options = $.extend({}, methods.defaults, options)
            $(this).loading "_open", options
            return

        _open: (options) ->
            markup = ""
            options.beforeOpen()
            unless options.visible
                options.visible = "invisible"
            else
                options.visible = ""
            options.color is "black"
            markup += "<div class=\"loading " + options.color + " " + options.type + "\" id=\"" + options.id + "\">"
            markup += "<div class=\"indicator\">" + options.type + "</div>"
            markup += "<span class=\"msg\">" + options.message + "</span>"
            markup += "</div>"
            $(this).addClass(options.visible)[options.appendType] markup
            options.afterOpen()
            return

        close: (options) ->
            options = $.extend({}, methods.defaults, options)
            options.beforeClose()
            if options.closeAll
                $(this).removeClass("invisible").find(".loading").remove()
            else
                $(this).removeClass("invisible").find(".loading").first().remove()
            options.afterClose()
            return

    $.fn.loading = (method) ->
        if methods[method]
            methods[method].apply this, Array::slice.call(arguments, 1)
            return
        else if typeof method is "object" or not method
            methods.init.apply this, arguments
            return
        else
            $.error "Method " + method + " does not exist on jQuery.Loading"
            return
    return
) jQuery