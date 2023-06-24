function removeCourt(id){
$.ajax({
        url:'/removeCourt',
        method:'post',
        data: {'id':id},
        success: function (response){
            location.reload();
            alert("Removed court succesfully")
        }
       })
}
function removeDayType(id){
$.ajax({
        url:'/removeDayType',
        method:'post',
        data: {'id':id},
        success: function (response){
            location.reload();
            alert("Removed Day Types succesfully")
        }
       })
}
function removeSpecialDate(id){
$.ajax({
        url:'/removeSpecialDates',
        method:'post',
        data: {'id':id},
        success: function (response){
            location.reload();
            alert("Removed Special Date succesfully")
        }
       })
}
function removeGames(id){
$.ajax({
        url:'/removeGames',
        method:'post',
        data: {'id':id},
        success: function (response){
            location.reload();
            alert("Removed game succesfully")
        }
       })
}function removeSlot(id){
 $.ajax({
         url:'/removeSlot',
         method:'post',
         data: {'id':id},
         success: function (response){
             location.reload();
             alert("Removed slot successfully")
         }
        })
 }
