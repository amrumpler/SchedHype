window.l = (string) -> string.toLocaleString()

class l10
    @classes = []

    constructor: () ->
        @labels = ['language']
        l10.init(this)

        @languages = [
            { code: 'en', name: 'English' },
            { code: 'fr', name: 'Francais'},
            { code: 'es', name: 'Espanol' }
        ]

        cookieValue = $.cookie('_language')
        @selectedLanguage = ko.observable(if cookieValue then cookieValue else '')
        @languageChanged null

    languageChanged: (event) ->
        $.cookie('_language', @selectedLanguage(), { expires: 50 })
        String.locale = @selectedLanguage()
        l10.updateLocale()

    @init: (clazz) ->
        l10.classes.push(clazz) unless clazz in l10.classes

        cname = clazz.constructor.toString().match(/function\s*(\w+)/)[1]

        # console.log "creating labels for #{cname}"

        for label in clazz.labels
            clazz["l_#{label}"] = ko.observable l("#{cname}.#{label}")
        null

    @updateLocale: () ->
        for clazz in l10.classes
            cname = clazz.constructor.toString().match(/function\s*(\w+)/)[1]
            for label in clazz.labels
                clazz["l_#{label}"] l("#{cname}.#{label}")

$ ->
    window.l10 = l10
    localeHandler = new l10
    if $('[id=languageForm]').length > 0
      ko.applyBindings(localeHandler, $('#languageForm').get(0))

    if typeof Function.empty == 'undefined'
        Function.empty = ()->;

    if typeof window.console == 'undefined'
        window.console =
            'log': Function.empty
            'debug': Function.empty
            'info': Function.empty
            'warn': Function.empty
            'error': Function.empty
            'assert': Function.empty
            'dir': Function.empty
            'dirxml': Function.empty
            'trace': Function.empty
            'group': Function.empty
            'groupCollapsed': Function.empty
            'groupEnd': Function.empty
            'time': Function.empty
            'timeEnd': Function.empty
            'profile': Function.empty
            'profileEnd': Function.empty
            'count': Function.empty
