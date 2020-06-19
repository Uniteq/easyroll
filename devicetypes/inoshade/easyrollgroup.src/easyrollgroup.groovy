/**
 *  EasyRollGroup
 *
 *  Copyright 2020 Inoshade
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
 
 /*EasyRollGroup DTH는 블라인드 여러개를 하나의 DTH로 제어하기 위한 템플릿코드 입니다.*/
 
import groovy.json.*
import groovy.json.JsonSlurper

metadata {
	definition (name: "EasyRollGroup", namespace: "Inoshade", author: "Nuovothoth", cstHandler: true) {
		capability "Switch"
		capability "Switch Level"
        capability "Momentary"
        capability "Health Check"
		capability "Polling"
		capability "Refresh"
        
        attribute "nameDev1", "string"
        attribute "nameDev2", "string"
        attribute "nameDev3", "string"
        attribute "nameDev4", "string"
        attribute "nameDev5", "string"
        
        attribute "locDev1", "number"
        attribute "locDev2", "number"
        attribute "locDev3", "number"
        attribute "locDev4", "number"
        attribute "locDev5", "number"

		command "up"
    	command "stop"
        command "down"
        command "jogUp"
        command "jogDown"
        command "autoLevel"
        
        command "m1"
        command "m2"
        command "m3"
        
        command "topSave"
        command "bottomSave"
        
        command "ref"
	}
	
    preferences {
    	//현재 템플릿 코드를 변경해서 디바이스 갯수 커스텀 가능
        input "easyrollAddress1", "text", type:"text", title:"IP Address 1",
            description: "enter easyroll address must be [ip]:[port] ", required: true
        input name: "ezActive1", type: "bool", title: "Active1", description: "On/Off", required: true
        
        input "easyrollAddress2", "text", type:"text", title:"IP Address 2",
            description: "enter easyroll address must be [ip]:[port] "
        input name: "ezActive2", type: "bool", title: "Active2", description: "On/Off"
        
        input "easyrollAddress3", "text", type:"text", title:"IP Address 3",
            description: "enter easyroll address must be [ip]:[port] "
        input name: "ezActive3", type: "bool", title: "Active3", description: "On/Off"
        
        input "easyrollAddress4", "text", type:"text", title:"IP Address 4",
            description: "enter easyroll address must be [ip]:[port] "
        input name: "ezActive4", type: "bool", title: "Active4", description: "On/Off"
        
        input "easyrollAddress5", "text", type:"text", title:"IP Address 5",
            description: "enter easyroll address must be [ip]:[port] "
        input name: "ezActive5", type: "bool", title: "Active5", description: "On/Off"
        
        input name: "setMode", type: "bool", title: "SetMode", description: "On/Off"
        input name: "stan", type: "enum", title: "AutoLevel Target", options: ["device1", "device2", "device3", "device4", "device5"], description: "choose standard of autoleveling", required: true
    }

	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2)  {
        standardTile("ref", "device.momentary", width: 6, height: 1, inactiveLabel: false, decoration: "flat") {
            state("ref", label: 'refresh', action: "ref")
        }
        
		standardTile("up", "device.momentary", width: 4, height: 2, inactiveLabel: false, decoration: "flat") {
            state("up", label: 'up', action: "up", icon: "st.samsung.da.oven_ic_up")
        }
        standardTile("jogUp", "device.momentary", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state("jogUp", label: 'jogUp', action: "jogUp", icon: "st.samsung.da.oven_ic_plus")
        }
        standardTile("stop", "device.momentary", width: 4, height: 2, inactiveLabel: false, decoration: "flat") {
            state("stop", label: 'stop', action: "stop", icon: "st.samsung.da.washer_ic_cancel")
        }
        standardTile("autoLevel", "device.momentary", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state("autoLevel", label: 'autoLevel', action: "autoLevel", icon: "st.contact.contact.closed")
        }
        standardTile("down", "device.momentary", width: 4, height: 2, inactiveLabel: false, decoration: "flat") {
            state("close", label: 'down', action: "down", icon: "st.samsung.da.oven_ic_down")
        }
        standardTile("jogDown", "device.momentary", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state("jogDown", label: 'jogDown', action: "jogDown", icon: "st.samsung.da.oven_ic_minus")
        }
        
        
        
        standardTile("m1", "device.momentary", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
            state("push", label: 'M1', action: "m1", icon: "st.illuminance.illuminance.dark")
        }
        standardTile("m2", "device.momentary", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
            state("push", label: 'M2', action: "m2", icon: "st.illuminance.illuminance.dark")
        }
        standardTile("m3", "device.momentary", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
            state("push", label: 'M3', action: "m3", icon: "st.illuminance.illuminance.dark")
        }
        
        
        valueTile("NameDev1", "device.nameDev1", width: 1, height: 1, decoration: "flat") {
            state "default", label:"Device1", defaultState: true
        }
        valueTile("NameDev2", "device.nameDev2", width: 1, height: 1, decoration: "flat") {
            state "default", label:"Device2", defaultState: true
        }
        valueTile("NameDev3", "device.nameDev3", width: 1, height: 1, decoration: "flat") {
            state "default", label:"Device3", defaultState: true
        }
        valueTile("NameDev4", "device.nameDev4", width: 1, height: 1, decoration: "flat") {
            state "default", label:"Device4", defaultState: true
        }
        valueTile("NameDev5", "device.nameDev5", width: 1, height: 1, decoration: "flat") {
            state "default", label:"Device5", defaultState: true
        }
        
        controlTile("blindLevel", "device.level", "slider", height: 2, width: 1, inactiveLabel: false) {
			state "level", action:"switch level.setLevel"
		}
        valueTile("valueDev1", "device.locDev1", width: 1, height: 1, decoration: "flat") {
            state "val", label:'${currentValue}', defaultState: true
        }
        valueTile("valueDev2", "device.locDev2", width: 1, height: 1, decoration: "flat") {
            state "default", label:'${currentValue}', defaultState: true
        }
        valueTile("valueDev3", "device.locDev3", width: 1, height: 1, decoration: "flat") {
            state "default", label:'${currentValue}', defaultState: true
        }
        valueTile("valueDev4", "device.locDev4", width: 1, height: 1, decoration: "flat") {
            state "locDev4", label:'${currentValue}', defaultState: true
        }
        valueTile("valueDev5", "device.locDev5", width: 1, height: 1, decoration: "flat") {
            state "default", label:'${currentValue}', defaultState: true
        }
        
        standardTile("topSave", "device.momentary", width: 3, height: 1, inactiveLabel: false, decoration: "flat") {
            state("push", label: "top save", action: "topSave")
        }
        standardTile("bottomSave", "device.momentary", width: 3, height: 1, inactiveLabel: false, decoration: "flat") {
            state("push", label: "bottom save", action: "bottomSave")
        }
	}
}
def installed() {
	log.debug("installed")
	state.locMap = [locDev1: 0, locDev2: 0, locDev3: 0, locDev4: 0, locDev5: 0]
}

def updated() {
	log.debug("updated")
	// Device-Watch simply pings if no device events received for 32min(checkInterval)
    sendEvent(name: "checkInterval", value: 30)
}

/*refresh: 블라인드 위치값을 알기 위해 사용(추후 polling으로도 사용 가능)*/
def ref() {
	//log.debug "refresh()"
    getCurData()
}
// parse events into attributes
def parse(String description) {
	//log.debug "Parsing '${description}'"
    def tempStr = description.split(';')
    createEvent(name: tempStr[0], value: tempStr[1].intValue()) 
}

/*HTTP REST API Request&response Handler*/
def runAction(String uri, String mode, def command){
	def options = [
        "method": "POST",
        "path": "${uri}",
        "headers": [
            "HOST": ""
        ],
        "body": '{'+
        	"mode:"+ "${mode}"+','+
            "command:"+ "${command}"+
        '}'
    ]
    
    for (int i = 0; i < 5; i++) {
    	def tempAddr = verifyAddr(i)
        //log.debug "host: ${addrName[i]}"
        if(tempAddr != null) { //선택된 디바이스만 동작(선택된 것 중에서도 ip정보가 기입되어 있는것만)
        	options.headers.put("HOST", tempAddr)
            //log.debug "host: ${tempAddr}"
            //log.debug "options: ${options}"
            def myhubAction = new physicalgraph.device.HubAction(options, null)
            sendHubCommand(myhubAction)
        }
    }
}

def getCurData() {
	def options = [
            "method": "GET",
            "path": "/lstinfo",
            "headers": [
                    "HOST": ""
            ]
    ]
    for (int i = 0; i < 5; i++) {
        def tempAddr = verifyAddr(i)
        //log.debug "getCurData host: ${tempAddr}"
        if(tempAddr != null) { //선택된 디바이스만 동작(선택된 것 중에서도 ip정보가 기입되어 있는것만)
            //log.debug "verified host: ${tempAddr}"
            options.headers.put("HOST", tempAddr)
            //log.debug "host: ${tempAddr}"
            //log.debug "options: ${options}"
            def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: fromHub]) //response를 이용하여 position을 display해야 하기 때문에 callback함수 호출
            sendHubCommand(myhubAction)
        }
    }
}

def getTargetData(){
	def devMap = ["device1": 0, "device2": 1, "device3": 2, "device4": 3, "device5": 4]
	def options = [
            "method": "GET",
            "path": "/lstinfo",
            "headers": [
                    "HOST": ""
            ]
    ]
    
    //log.debug "targetsss: $stan"
    def tempAddr = verifyAddr(devMap["$stan"])
    if(tempAddr != null) { 
        options.headers.put("HOST", tempAddr)
        def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: fromHubforAutolevel]) 
        sendHubCommand(myhubAction)
    }
}

def ping() {
	log.debug("ping")
	getCurData()
}

def verifyAddr(num) {
	def addrName = [easyrollAddress1, easyrollAddress2, easyrollAddress3, easyrollAddress4, easyrollAddress5]
    def activeBool = [ezActive1, ezActive2, ezActive3, ezActive4, ezActive5]
	//log.debug("activeBool[$num]: ${activeBool[num]}")
    if(addrName[num] != null && addrName[num].endsWith(":20318") && activeBool[num] == true){
    	return addrName[num]
    }else{
    	return null
    }
}

def fromHubforAutolevel(physicalgraph.device.HubResponse hubResponse){
	//log.debug "fromHubforAutolevel()"
	def msg

	try {
        msg = parseLanMessage(hubResponse.description)

        def resp = new JsonSlurper().parseText(msg.body)
		//log.debug "fromHub Parsing '${resp}'"
        //log.debug "fromHub Parsing ${deviceMap[resp.local_ip]}"

        try{
            def positionVal = resp.position.intValue()
            log.debug "positionVal: $positionVal"
            if (positionVal<1) { //최상단으로 갈 때에는 음수로 측정되는 경우가 발생할 수 있기 때문에 처리해줌
                positionVal = 0 
            }else if(positionVal>=100){
                positionVal = 100 
            }
            runAction("/action", "level", positionVal)
        }catch(e){
        	log.debug "error: $e"
        }
    } catch (e) {
        log.error "Exception caught while parsing data: "+e;
    }
}

def fromHub(physicalgraph.device.HubResponse hubResponse){
	//log.debug "callbackHub()"
    def addrName = [easyrollAddress1, easyrollAddress2, easyrollAddress3, easyrollAddress4, easyrollAddress5]
    def deviceMap = [(easyrollAddress1): "locDev1", (easyrollAddress2): "locDev2", (easyrollAddress3): "locDev3", (easyrollAddress4): "locDev4", (easyrollAddress5): "locDev5"]
    def msg

	try {
        msg = parseLanMessage(hubResponse.description)

        def resp = new JsonSlurper().parseText(msg.body)
		//log.debug "fromHub Parsing '${resp}'"
        //log.debug "fromHub Parsing ${deviceMap[resp.local_ip]}"
		
        try{
        	def attrName = deviceMap[resp.local_ip]
            //log.debug "attrName '${attrName}'"
            if(attrName != null ){
                def positionVal = resp.position.intValue()
                //log.debug "positionVal: $positionVal"
                if (positionVal<1) { //최상단으로 갈 때에는 음수로 측정되는 경우가 발생할 수 있기 때문에 처리해줌
                    positionVal = 0 
                }else if(positionVal>=100){
                    positionVal = 100 
                }
                positionVal = 100-positionVal
                //log.debug "key: $attrName"
                state.locMap[attrName] = positionVal
                //log.debug "state.locMap[$attrName]: ${state.locMap[attrName]}"
                sendEvent(name: attrName, value: positionVal) 
            }
        }catch(e){
        	log.debug "error: $e"
        }
    } catch (e) {
        log.error "Exception caught while parsing data: "+e;
    }
}

/*명령어 실행 함수들*/
//퍼센트 이동 명령
def setLevel(value, rate = null) {
	//log.trace "setLevel($value)"
	if (setMode == false) {
    	runAction("/action", "level", 100-value) //EasyRoll Device는 0%가 최상단, 100%가 최하단이기 때문
    }
}
//올리기(세팅모드일때는 강제올림)
def up() {
	//log.debug "Executing 'up'"
    if (setMode == true) {
    	runAction("/action", "force", "FTU")
    } else {
    	runAction("/action", "general", "TU")
    }  
}
//멈춤
def stop() {
	//log.debug "Executing 'stop'"
    if (setMode == true) {
    	runAction("/action", "force", "FSS")
    } else {
    	runAction("/action", "general", "SS")
    } 
}
//내리기(세팅모드일때는 강제내림)
def down() {
	//log.debug "Executing 'down'"
    if (setMode == true) {
        runAction("/action", "force", "FBD")
    } else {
    	runAction("/action", "general", "BD")
    } 
}
//한 칸 올리기(세팅모드일때는 강제 한 칸 올리기)
def jogUp() {
	//log.debug "Executing 'jogUp'"
    if (setMode == true) {
        runAction("/action", "force", "FSU")
    } else {
    	runAction("/action", "general", "SU")
    } 
}
//한 칸 내리기(세팅모드일때는 강제 한 칸 내리기)
def jogDown() {
	//log.debug "Executing 'jogDown'"
    if (setMode == true) {
        runAction("/action", "force", "FSD")
    } else {
    	runAction("/action", "general", "SD")
    } 
}
//선택된 블라인드 끼리의 위치 동일하게 맞추기
//블라인드 중 가장 아래쪽에 있는 블라인드 기준으로 정렬됨.
def autoLevel() {
	//log.debug "Executing 'autoLevel'"
    if (setMode == false) { //autoLevel은 강제모드에서 실행할 수 없음(최하단, 최상단을 이용하기 때문)
        stop() //현재 위치를 측정해야 하기 때문에 모든 블라인드는 정지 상태가 되어야 함
        runIn(1, getTargetData) //현재 위치 측정
    }
}

//메모리1 이동 (세팅 모드 시 현재 위치 메모리1에 저장)
def m1() {
	//log.debug "Executing 'm1'"
    if (setMode == true) {
        runAction("/action", "save", "SM1")
    } else {
    	runAction("/action", "general", "M1")
    } 
}
//메모리2 이동 (세팅 모드 시 현재 위치 메모리2에 저장)
def m2() {
	//log.debug "Executing 'm2'"
    if (setMode == true) {
        runAction("/action", "save", "SM2")
    } else {
    	runAction("/action", "general", "M2")
    } 
}
//메모리3 이동 (세팅 모드 시 현재 위치 메모리3에 저장)
def m3() {
	//log.debug "Executing 'm3'"
    if (setMode == true) {
        runAction("/action", "save", "SM3")
    } else {
    	runAction("/action", "general", "M3")
    } 
}
//최상단, 최하단은 세팅모드에서만 동작하도록 함
def bottomSave() {
	//log.debug "Executing 'bottomSave'"
    if (setMode == true) {
        runAction("/action", "save", "SB")
    }
}
def topSave() {
	//log.debug "Executing 'topSave'"
    if (setMode == true) {
        runAction("/action", "save", "ST")
    }
}
