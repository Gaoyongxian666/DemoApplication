var $table = $('#table_datas');
var rowId='';
var url = '';

$(function(){
    $.ajax({
        url: url + "/getTitle",
        type:'get',
        success: function(result) {
            var result = JSON.parse(result);
            if(result.isSuccessful){
                var data = result.datas[0];
                $('#title').val(data.title);
                $('#showcolumn option').eq(data.columns_number-1).attr('selected',true);
                $('#fwip').val(data.comserverip);
                $('#fwdk').val(data.comserverport);
                $('#text').val(data.text);
                if(data.background!=''){
                    $('#upload-backImg').append("<img src = '"+data.background+"' height = '100%' width = '100%' />")
                }
            }
        }
    });
    getTablesDatas();
})

/**
 * 添加服务配置
 */
function addServe(){
    var background = typeof($('#upload-backImg img').attr('src')) == 'undefined' ? "" : ($('#upload-backImg img').attr('src').split('=')[1]);
    var title = $('#title').val();
    var columns_number = $('#showcolumn').val();
    var comserverip = $('#fwip').val();
    var comserverport = $('#fwdk').val();
    var text = $('#text').val();
    if(title==''||comserverip==''||comserverport==''||text==''||background==''){
        toastr.error("请将信息填写完整");
        return;
    }
    var field = 'title='+ title +'&columns_number='+ columns_number +'&comserverip='+ comserverip +'&comserverport='+ comserverport +'&background='+ background + '&text='+ text;

    $.ajax({
        url: url+"/setTitle",
        data:field,
        success: function(result) {
            console.log(result)
            result = JSON.parse(result);
            if(!result.isSuccessful){
                toastr.error("添加数据失败",result.error);
                return;
            }
            toastr.success("创建成功");
        }
    });
}
/**
 * 添加图片
 */
function uploadFile(obj){
    var upload = obj.id;
    if(checkImgType(document.getElementById(upload).files[0])){
        var formData = new FormData();
        formData.append("file", document.getElementById(upload).files[0]);
        $.ajax({
            url: url + "/upload",
            type:'POST',
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function (result){
                // var result = JSON.parse(result);
                // if(!result.isSuccessful){
                //     toastr.error("上传失败");
                //     return;
                // }
                var ht = "<img src = '"+result+"' height = '100%' width = '100%' />";
                $("#"+upload+"-backImg").empty();
                $("#"+upload+"-backImg").append(ht);
            }
        });
    }
}
//校验图片
function checkImgType(ths){
    if (ths.value == "") {
        return false;
    }else {
        if(!/\.(jpg|jpeg|png|JPG|JPEG|PNG)$/.test(ths.name)) {
            toastr.error("图片类型必须是jpeg,jpg,png中的一种");
            ths.value = "";
            return false;
        }else{
            var size = ths.size;
            var max_size = Math.ceil(size / 1024);
            if(max_size > 5*1024){
                toastr.error("图片不能大于5M");
                return false;
            }
        }
    }
    return true;
}

/**
 * 创造添加数据的表单
 */
function addDataform() {
    $("#add_modal").modal('show');
}
/**
 * 添加数据
 */
function addData() {
    var londId = $('#londId').val();
    var yinImg = typeof($('#upload2-backImg img').attr('src')) == 'undefined' ? '' : ($('#upload2-backImg img').attr('src').split('=')[1]);
    var timeLand = $('#timeLand').val();
    if(londId==''&&timeLand==''&&yinImg==''){
        toastr.error("添加的数据不能为空");
        return;
    }

    var field = 'id='+ londId +'&img_path='+ yinImg +'&time='+ timeLand;
    $.ajax({
        url: url+"/addPath",
        data:field,
        success: function(result) {
            console.log(result)
            var result = JSON.parse(result);
            if(!result.isSuccessful){
                toastr.error("添加数据失败");
                return;
            }
            document.getElementById("route").reset();
            $("#upload2-backImg").empty();
            $("#add_modal").modal('hide');
            getTablesDatas();
            toastr.success("添加数据成功");
        }
    });
}
/**
 * 删除路径数据
 */
window.operateEvents = {
    'click .remove': function (e, value, row, index) {
        $('#del_modal').modal('show');
        rowId = row.id;
    }
}
function delData() {
    $("#del_modal").modal('hide');
    $.ajax({
        url: url+"/delPath",
        data:'id='+rowId,
        success: function(result) {
            console.log(result)
            var result = JSON.parse(result);
            if(result.isSuccessful){
                $table.bootstrapTable('remove', {
                    field: 'id',
                    values: [rowId]
                })
            }else{
                toastr.error("删除失败");
                return;
            }
        }
    });
}
/**
 * 获取路径数据
 */
function operateFormatter(value, row, index) {
    return [
        '<a class="remove" href="javascript:void(0)" title="Remove">',
        '删除',
        '</a>'
    ].join('')
}

function imgFormatter(value, row, index){
    return [
        '<img src="'+row.img_path+'" width="50px" height="50px" />'
    ].join('')
}
function getTablesDatas() {
    $.ajax({
        url: url+"/getPathList",
        success: function(result) {
            var tableList = JSON.parse(result);
            $table.bootstrapTable('destroy').bootstrapTable({ //'destroy' 是必须要加的==作用是加载服务器//  //数据，初始化表格的内容Destroy the bootstrap table.
                data: tableList.datas, //datalist 即为需要的数据
                dataType: 'json',
                toolbar: '#toolbar',
                data_locale: "zh-US",
                uniqueId: "id",
                paginationLoop: false,
                //这里也可以将TABLE样式中的<tr>标签里的内容挪到这里面：
                columns: [{
                    field: 'id',
                    title: '路径ID',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'img_path',
                    title: '引导图片',
                    align: 'center',
                    formatter: imgFormatter
                }, {
                    field: 'time',
                    title: '亮灯时长',
                    align: 'center',
                    valign: 'middle'
                },{
                    field: 'operate',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    events: window.operateEvents,
                    formatter: operateFormatter
                }]
            });
        }
    });

}

