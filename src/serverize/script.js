$.fn.consolize = function(commandHandler) {
  var self = {
    output: '',
    command: '',
    waiting: '' // the command sent to the server but not yet responded
  };
  
  var $inner = $('<pre class="consolized-inner"></pre>');
  var $input = $('<textarea class="consolized-input"></textarea>');
  
  function redraw() {
    var parts = [];
    for (i = 0; i < self.output.length; i++) {
      if (self.output[i] === "\r" || self.output[i] === "\n") {
        parts.push('<span class="linebreak"></span>');
        if (self.output[i] === "\r" && self.output[i + 1] === "\n") i++;
      }
      else {
        parts.push('<span class="char">' + self.output[i] + '</span>');
      }
    }
    for (i = 0; i < self.waiting.length; i++) {
      parts.push('<span class="waiting char">' + self.waiting[i] + '</span>');
    }
    for (i = 0; i < self.command.length; i++) {
      parts.push('<span class="command char">' + self.command[i] + '</span>');
    }
    parts.push('<span class="cursor"> </span>');
    $inner.html(parts.join(''));
  }
  
  this.append($inner).append($input);
  
  $input.focus().keydown(function(e) {
    if (jwerty.is('backspace', e)) {
      self.command = self.command.slice(0, -1);
    }
    redraw();
  }).keypress(function(e) {
    if (jwerty.is('enter', e)) {
      commandHandler(self.command);
      self.waiting = self.command;
      self.command = '';
      $input.val('');
      e.preventDefault();
    }
    else {
      self.command += String.fromCharCode(e.which);
    }
    redraw();
  });
  
  setInterval(function(){
    $('.cursor').toggleClass('blink');
  }, 500);
  
  return $.extend(this, {
    output: function(data) {
      self.output += data;
      self.waiting = '';
      redraw();
    }
  });
};

$(function(){
  var $c = $('#console').consolize(function(command) {
    $.post('/input', { sessionId: window.sessionId, input: command });
  });
  
  setInterval(function() {
    $.get('/output', { sessionId: window.sessionId }, $c.output);
  }, 1000);
  
  window.onbeforeunload = function() {
    $.post('/unload', { sessionId: window.sessionId });
  };
});
