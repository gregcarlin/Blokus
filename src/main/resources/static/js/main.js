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

var updateStatus = function(e) {
  var text = e ? $(this).attr('title')
  : $('.form-create .btn.active').attr('title');
  $('#status').html(text);
};
$('.form-create .btn').click(updateStatus);

var unitMult = 1;
$(function() {
  // enable tooltips
  $('[data-toggle="tooltip"]').tooltip();

  // change timer units
  $('.dropdown-timer a').click(function() {
    var me = $(this);
    $('#unit').html(me.html());
    unitMult = me.attr('data-mult');
  });

  // set actual timer before form submission
  $('.form-create').submit(function() {
    $('#timer-actual').val($('#timer').val() * unitMult);
  });

  updateStatus();
});
