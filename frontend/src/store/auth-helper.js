import VueJwtDecode from "vue-jwt-decode";
import router from "../router";

export default class AuthHelper {
    static jwtTokenName = "tk";

    static readAuthFromLocalStorage()
    {
        try {
            const jwtToken = localStorage.getItem(this.jwtTokenName)
            return jwtToken ? VueJwtDecode.decode(jwtToken).authorities.split(',') : [];
        } catch (error) {
            console.warn(error)
            return []
        }
    }

    static checkAuthority(targetAuthority) {
        return this.readAuthFromLocalStorage().includes(targetAuthority);
    }

    static isAdmin() {
        return this.checkAuthority("Admin");
    }

    static isUser() {
        return this.checkAuthority("User");
    }

    static isExpiredTokenCheck(data) {
        if (data.response.status === 401) {
            router.push('/login');
            return;
        }
    }

    static getParamsWithAuth(params) {
        let tmpParams = {};
        if (params !== undefined) {
            tmpParams['params'] = params;
        }

        tmpParams['headers'] = {
            Authorization: 'Bearer ' + localStorage.getItem(this.jwtTokenName)
        };

        return tmpParams;
    }
}