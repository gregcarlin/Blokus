var timerVal = 60;
$('#timer-btn').click(function() {
  var timer = $('.input-group-timer');
  if (timer.is(':visible')) {
    timer.css('display', 'none');
    timerVal = $('#timer').val();
    $('#timer').val(0);
    $('#timer-btn').html('Add Timer');
  } else {
    timer.css('display', 'table');
    $('#timer').val(timerVal);
    $('#timer-btn').html('Remove Timer');
  }
});

$(function() {
  $('[data-toggle="tooltip"]').tooltip();
});
