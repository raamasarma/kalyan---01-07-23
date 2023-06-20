function showSlots(code){
    $("."+code).toggle();
        $(".request-"+code).toggle();

}
function approveSlot(id){


    $.ajax({
        url:'/approveSlot',
        method:'post',
        data: {'id':id,'remarks':$('#remarksAdm-'+id).val()},
        success: function (response){
            if (response.status == 202){
            location.reload();
            }
            alert(response.msg);
        }
       })
}

function placeRequest(slotCode, userId,userMobile, slotId){
       const myModal = document.getElementById('modal');
       const myInput = document.getElementById('referredBy');

       myModal.addEventListener('shown.bs.modal', () => {
         myInput.focus()
       })
       $('#modal').on('hidden.bs.modal', function () {
            $.ajax({
                    url:'/requestSlot',
                    method:'post',
                    data: {'slotCode':slotCode, 'userId':userId,'userMobile':userMobile, 'slotId':slotId, 'date': $("#bookingDate").val(), 'remarks': $("#remarks").val(),'referredBy': $("#referredBy").val(),},
                    success: function (response){
                        if (response.status == 202){
                        window.location.pathname = "/myRequests";
                        }
                        alert(response.msg);
                    }
                   })
       });

}

$("document").ready(
function(){
    $("#dateacc, #courtacc").change(
        function(){
        $("#"+$('#dateacc').attr('orig')).toggle();
               $("#"+$('#dateacc').val()+'--'+ $('#courtacc').val()).toggle();

        $('#courtacc').attr('orig',$('#dateacc').val()+'--'+ $('#courtacc').val());
        $('#dateacc').attr('orig',$('#dateacc').val()+'--'+ $('#courtacc').val());

        })
    }

)
function showRequests(slotCode){
$(".requests").css("display", "none");
     $(".request-"+slotCode).toggle();
 }