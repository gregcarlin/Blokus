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

const icons = ['globe', 'lock', 'laptop'];
const tools = ['Public', 'Private', 'Local'];
const stIcons = ['clock-o', 'play', 'check'];
const stTools = ['Waiting for players', 'Playing', 'Completed'];
var update = true;
var updateTables = function() {
  if (!update) return;
  $.get('list', function(data) {
    var json = JSON.parse(data);

    var currBody = $('.table-current tbody');
    if (json.current.length) {
      currBody.html('');
      _.each(json.current, function(game) {
        var html =
          '<tr onclick="window.location=\'/auth/play/' + game._id + '\'">' +
            '<td><span class="fa fa-fw fa-' + icons[game.params.privacy] +
              '" data-toggle="tooltip" data-placement="left" title="' +
              tools[game.params.privacy] + '"></span>' +
              '<span class="fa fa-fw fa-' + stIcons[game.state] +
              '" data-toggle="tooltip" data-placement="right" title="' +
              stTools[game.state] + '"></span></td>' +
            '<td><ul>';
        _.each(game.players, function(player) {
          html += '<li>' + player + '</li>';
        });
        html += '</ul></td>' +
          '<td>' + game.params.num_players + '</td>' +
          '<td>' + game.params.timer + '</td>' +
          '</tr>';
        currBody.append(html);
      });
    } else {
      currBody.html('<tr><td colspan="4">You are not currently in any games.</td></tr>');
    }

    var pubBody = $('.table-public tbody');
    if (json['public'].length) {
      pubBody.html('');
      _.each(json['public'], function(game) {
        var html =
          '<tr onclick="window.location=\'/auth/join/' + game._id + '\'">' +
            '<td><ul>';
        _.each(game.players, function(player) {
          html += '<li>' + player + '</li>';
        });
        html += '</ul></td>' +
          '<td>' + game.params.num_players + '</td>' +
          '<td>' + game.params.timer + '</td>' +
          '</tr>';
        pubBody.append(html);
      });
    } else {
      pubBody.html('<tr><td colspan="3">You are not currently in any games.</td></tr>');
    }

    // enable tooltips
    $('[data-toggle="tooltip"]').tooltip();

    // do this again in 5 seconds
    setTimeout(updateTables, 5000);
  });
};

var unitMult = 1;
$(function() {
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
  updateTables();
});
