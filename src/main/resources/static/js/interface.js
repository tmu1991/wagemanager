var prefix = 'http://192.168.43.5:8006/'
/*
           FileReader共有4种读取方法：
           1.readAsArrayBuffer(file)：将文件读取为ArrayBuffer。
           2.readAsBinaryString(file)：将文件读取为二进制字符串
           3.readAsDataURL(file)：将文件读取为Data URL
           4.readAsText(file, [encoding])：将文件读取为文本，encoding缺省值为'UTF-8'
                        */
var wb; //读取完成的数据
var rABS = false; //是否将文件读取为二进制字符串

function importf(obj) { //导入
	if(!obj.files) {
		return;
	}
	var f = obj.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		var data = e.target.result;
		if(rABS) {
			wb = XLSX.read(btoa(fixdata(data)), { //手动转化
				type: 'base64'
			});
		} else {
			wb = XLSX.read(data, {
				type: 'binary'
			});
		}
		//wb.SheetNames[0]是获取Sheets中第一个Sheet的名字
		//wb.Sheets[Sheet名]获取第一个Sheet的数据
		localStorage.dome = JSON.stringify(XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]));
	};
	if(rABS) {
		reader.readAsArrayBuffer(f);
	} else {
		reader.readAsBinaryString(f);
	}
}

function fixdata(data) { //文件流转BinaryString
	var o = "",
		l = 0,
		w = 10240;
	for(; l < data.byteLength / w; ++l) o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w, l * w + w)));
	o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)));
	return o;
}

function individual(n, m) {
	//n代表工资，m代表五险一金
	var n = Number(n);
	var m = Number(m);
	if(n > 3500) {
		//应纳税额
		var num = n - m - 3500;
		if(num > 0 && num <= 1500) {
			//小于1500，应纳3%,减速算0
			var tax = (num * 0.03) - 0;
		} else if(num > 1500 && num <= 4500) {
			//应纳10%，扣105
			var tax = num / 10;
			tax = tax - 105;
		} else if(num > 4500 && num <= 9000) {
			//应纳20%，扣555
			var tax = (num * 0.2) - 555;
		} else if(num > 9000 && num <= 35000) {
			//应纳25%，扣1005
			var tax = (num * 0.25) - 1005;
		} else if(num > 35000 && num <= 55000) {
			//应纳30%，扣2775
			var tax = (num * 0.3) - 2775;
		} else if(num > 55000 && num <= 80000) {
			//应纳35%，扣5505
			var tax = (num * 0.35) - 5505;
		} else if(num > 80000) {
			//应纳45%，扣13505
			var tax = (num * 0.45) - 13505;
		}
	} else {
		var tax = 0;
		var num = 0;
	}
	tax = tax.toFixed(2);
	num = num - tax + 3500;
	num = num.toFixed(2);
	return tax;
}

