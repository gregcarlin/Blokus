$('.form-signin input:visible').bind('change paste keyup', function() {
  var me = $(this);
  var newVal = me.val();
  var field = me.attr('name');
  $('.form-signin input[name="' + field + '"]').each(function() {
    var self = $(this);
    if (self.val() != newVal) self.val(newVal);
  });
});
