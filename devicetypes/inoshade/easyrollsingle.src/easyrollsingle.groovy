/**
 *  EasyRollSingle
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

 /*EasyRollSingle DTH는 단일 블라인드를 DTH로 제어하기 위한 템플릿코드 입니다.*/
 
import groovy.json.*
import groovy.json.JsonSlurper

metadata {
	definition (name: "EasyRollSingle", namespace: "Inoshade", author: "Nuovothoth", cstHandler: true) {
		capability "Switch"
		capability "Switch Level"
        capability "Momentary"
        
        attribute "switchDev1", "string"
        
        attribute "locDev1", "number"

		command "up"
    	command "stop"
        command "down"
        command "jogUp"
        command "jogDown"
        
        command "m1"
        command "m2"
        command "m3"
        
        command "topSave"
        command "bottomSave"
        
        command "onDev1"
        command "offDev1"
        
        command "init"
        command "refresh"
        command "setOn"
        command "setOff"
	}
	
    preferences {
        input "easyrollAddress1", "text", type:"text", title:"IP Address 1",
            description: "enter easyroll address must be [ip]:[port] ", required: true
    }

	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2)  {
    	valueTile("valueDev1", "device.locDev1", width: 2, height: 1, decoration: "flat") {
            state "default", label:'${currentValue}', defaultState: true
        }
        
        standardTile("refresh", "device.momentary", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
            state("refresh", label: 'refresh', action: "refresh")
        }
        standardTile("setMode", "device.switch", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
            state "off", label: "setOff", action:"setOn", backgroundColor:"#ffffff", nextState:"on"
            state "on", label: "setOn", action:"setOff", backgroundColor:"#00a0dc", nextState:"off"
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
        standardTile("jogDown", "device.momentary", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state("jogDown", label: 'jogDown', action: "jogDown", icon: "st.samsung.da.oven_ic_minus")
        }
        standardTile("down", "device.momentary", width: 4, height: 2, inactiveLabel: false, decoration: "flat") {
            state("close", label: 'down', action: "down", icon: "st.samsung.da.oven_ic_down")
        }
        controlTile("blindLevel", "device.level", "slider", height: 2, width: 2, inactiveLabel: false) {
			state "level", action:"switch level.setLevel"
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

        standardTile("topSave", "device.momentary", width: 3, height: 1, inactiveLabel: false, decoration: "flat") {
            state("push", label: "top save", action: "topSave")
        }
        standardTile("bottomSave", "device.momentary", width: 3, height: 1, inactiveLabel: false, decoration: "flat") {
            state("push", label: "bottom save", action: "bottomSave")
        }
	}
}

/*세팅모드 on/off 함수*/
//setMode 변수와 set 토글 버튼의 상태가 일치하지 않는 경우가 발생할 수 있습니다.
def setOn() {
	state.setMode = "true"
}

def setOff() {
	state.setMode = "false"
}

/*refresh: 블라인드 위치값을 알기 위해 사용(추후 polling으로도 사용 가능)*/
def refresh() {
	//log.debug "refresh()"
    getCurData()
}
// parse events into attributes
def parse(String description) {
	//log.debug "Parsing '${description}'"
}

/*HTTP REST API Request&response Handler*/
def runAction(String uri, String mode, def command){
	def options = [
        "method": "POST",
        "path": "${uri}",
        "headers": [
            "HOST": "${easyrollAddress1}"
        ],
        "body": '{'+
        	"mode:"+ "${mode}"+','+
            "command:"+ "${command}"+
        '}'
    ]
    
    def myhubAction = new physicalgraph.device.HubAction(options, null)
    sendHubCommand(myhubAction)
}

def getCurData() {
	def options = [
            "method": "GET",
            "path": "/lstinfo",
            "headers": [
                    "HOST": "${easyrollAddress1}"
            ]
    ]
    //log.debug "options: ${options}"
    
    def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: fromHub])
    sendHubCommand(myhubAction)
}

def fromHub(physicalgraph.device.HubResponse hubResponse){
	//log.debug "callbackHub()"
    def msg
    try {
        msg = parseLanMessage(hubResponse.description)

        def resp = new JsonSlurper().parseText(msg.body)
		log.debug "Parsing '${resp}'"
        log.debug "Parsing '${resp.local_ip}'"
        
        if(easyrollAddress1 == resp.local_ip) {
            log.debug "sendEvent"
            sendEvent(name: "locDev1", value: 100-resp.position.intValue())
        }
    } catch (e) {
        log.error "Exception caught while parsing data: "+e;
    }
}

/*명령어 실행 함수들*/
//퍼센트 이동 명령
def setLevel(value, rate = null) {
	//log.trace "setLevel($value)"
	if (setMode != "true") {
    	runAction("/action", "level", 100-value)
    }
}
//올리기(세팅모드일때는 강제올림)
def up() {
	//log.debug "Executing 'up'"
    if (state.setMode == "true") {
    	runAction("/action", "force", "FTU")
    } else {
    	runAction("/action", "general", "TU")
    }  
}
//멈춤
def stop() {
	//log.debug "Executing 'stop'"
    if (state.setMode == "true") {
    	runAction("/action", "force", "FSS")
    } else {
    	runAction("/action", "general", "SS")
    } 
}
//내리기(세팅모드일때는 강제내림)
def down() {
	//log.debug "Executing 'down'"
    if (state.setMode == "true") {
        runAction("/action", "force", "FBD")
    } else {
    	runAction("/action", "general", "BD")
    } 
}
//한 칸 올리기(세팅모드일때는 강제 한 칸 올리기)
def jogUp() {
	//log.debug "Executing 'jogUp'"
    if (state.setMode == "true") {
        runAction("/action", "force", "FSU")
    } else {
    	runAction("/action", "general", "SU")
    } 
}
//한 칸 내리기(세팅모드일때는 강제 한 칸 내리기)
def jogDown() {
	//log.debug "Executing 'jogDown'"
    if (state.setMode == "true") {
        runAction("/action", "force", "FSD")
    } else {
    	runAction("/action", "general", "SD")
    } 
}
//메모리1 이동 (세팅 모드 시 현재 위치 메모리1에 저장)
def m1() {
	//log.debug "Executing 'm1'"
    if (state.setMode == "true") {
        runAction("/action", "save", "SM1")
    } else {
    	runAction("/action", "general", "M1")
    } 
}
//메모리2 이동 (세팅 모드 시 현재 위치 메모리2에 저장)
def m2() {
	//log.debug "Executing 'm2'"
    if (state.setMode == "true") {
        runAction("/action", "save", "SM2")
    } else {
    	runAction("/action", "general", "M2")
    } 
}
//메모리3 이동 (세팅 모드 시 현재 위치 메모리3에 저장)
def m3() {
	//log.debug "Executing 'm3'"
    if (state.setMode == "true") {
        runAction("/action", "save", "SM3")
    } else {
    	runAction("/action", "general", "M3")
    } 
}
//최상단, 최하단은 세팅모드에서만 동작하도록 함
def bottomSave() {
	//log.debug "Executing 'bottomSave'"
    if (state.setMode == "true") {
        runAction("/action", "save", "SB")
    }
}
def topSave() {
	//log.debug "Executing 'topSave'"
    if (state.setMode == "true") {
        runAction("/action", "save", "ST")
    }
}
