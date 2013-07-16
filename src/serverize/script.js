$(function() {
  var $inputbox = $('#inputbox');
  var $console = $('#console');
  
  $inputbox.focus().keypress(function(e) {
    if (e.which === 13) {
      e.preventDefault();
      $.post('/input', { sessionId: window.sessionId, input: $inputbox.val() });
      $inputbox.val('');
    }
  });
  
  setInterval(function() {
    $.get('/output', { sessionId: window.sessionId }, function(data) {
      $console.val($console.val() + data);
      $inputbox.focus();
    });
  }, 1000);
  
  window.onbeforeunload = function() {
    $.post('/unload', { sessionId: window.sessionId });
  };
});