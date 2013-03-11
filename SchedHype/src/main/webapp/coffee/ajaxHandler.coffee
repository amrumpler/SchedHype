class AjaxHandler

  constructor: () ->
    @labels = ['waitMessage']
    @requestsSent = 0
    @requestsCompleted = 0

    l10.init(this)
    
    @is_smartphone = false
    if $("html").hasClass("touch") && $(window).width() < 480
      @is_smartphone = true
      self.scrollTo(0, 0)


    $.ajaxPrefilter (options, originalOptions, jqXHR) ->
      if options.container? then jqXHR.container = options.container
      if options.handleErrors? then jqXHR.handleErrors = options.handleErrors else jqXHR.handleErrors = true
      if options.errorFieldMap? then jqXHR.errorFieldMap = options.errorFieldMap
      if options.loadingMessage? then jqXHR.loadingMessage = options.loadingMessage
      if options.errorAppendType? then jqXHR.errorAppendType = options.errorAppendType
      return undefined # must set to undefined otherwise jquery will try to parse the response as the returned value

    $.ajaxSetup
      beforeSend: @beforeSend
      complete: @complete
      error: (jqXHR, textStatus, errorThrown) ->
        if jqXHR.handleErrors
          if jqXHR.status is 403 # Insufficient privileges, so push back to the landing page
            currentLocation?.redirectToLandingPage()

          if jqXHR.container?
            data = []
            try
              data = JSON.parse jqXHR.responseText
            catch ex
              data = errors: [l('Generic.error')]
  
            errorlist = ''
            for error in data.errors
              key = error.split('.')[1]
  
              if key.indexOf('Required') == -1
                errorlist += '<li>' + l(error) + '</li>'
  
              if jqXHR.errorFieldMap?[key]?
                $(jqXHR.errorFieldMap[key]).addClass('errorField').closest('p').addClass('errorField')
  
            jqXHR.container.notify
              message: '<ul>' + errorlist + '</ul>'
              sticky: true
              type: 'error'
              appendType: if jqXHR.errorAppendType? then jqXHR.errorAppendType else 'before'

  beforeSend: (jqXHR, settings) =>
    @requestsSent++
    if jqXHR.container?
      jqXHR.container.css {cursor: 'wait'}
      if jqXHR.loadingMessage?
        jqXHR.container.loading {message: jqXHR.loadingMessage}
      else
        jqXHR.container.loading()
    else
      $(document.body).css {cursor: 'wait'}

  complete: (jqXHR, status) =>
    @requestsCompleted++
    if jqXHR.container?
      jqXHR.container.css {cursor: ''}
      jqXHR.container.loading('close')
    else
      $(document.body).css {cursor: ''}

$ ->
  $.urlPrefix = '/' + location.pathname.split('/')[1]
  if $.urlPrefix == '/' || $.urlPrefix == '/auth' || location.pathname.indexOf('/',1) == -1
      $.urlPrefix = ''
  ajaxHandler = new AjaxHandler
  window.ajaxHandler = ajaxHandler
