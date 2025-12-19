// 封装fetch请求，统一处理前后端通信
const BASE_URL = 'http://localhost:8080';

// 请求配置
const config = {
    timeout: 10000, // 10秒超时
};

/**
 * 获取token
 */
function getToken() {
    return localStorage.getItem('token') || '';
}

// 统一处理响应
async function handleResponse(response) {
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
        return await response.json();
    } else {
        return await response.text();
    }
}

// 通用GET请求 - 在请求头中携带token
export async function get(url, params = {}) {
    // 构建查询参数
    const queryString = new URLSearchParams(params).toString();
    const fullUrl = queryString ? `${BASE_URL}${url}?${queryString}` : `${BASE_URL}${url}`;
    
    const response = await fetch(fullUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'token': getToken()
        }
    });
    
    return handleResponse(response);
}

// 通用POST请求 - 在请求头中携带token
export async function post(url, data = {}) {
    const response = await fetch(`${BASE_URL}${url}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'token': getToken()
        },
        body: JSON.stringify(data)
    });
    
    return handleResponse(response);
}

// 通用PUT请求 - 在请求头中携带token
export async function put(url, data = {}) {
    const response = await fetch(`${BASE_URL}${url}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'token': getToken()
        },
        body: JSON.stringify(data)
    });
    
    return handleResponse(response);
}

// 通用DELETE请求 - 在请求头中携带token
export async function del(url, params = {}) {
    // 构建查询参数
    const queryString = new URLSearchParams(params).toString();
    const fullUrl = queryString ? `${BASE_URL}${url}?${queryString}` : `${BASE_URL}${url}`;
    
    const response = await fetch(fullUrl, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'token': getToken()
        }
    });
    
    return handleResponse(response);
}

// FormData POST请求 - 用于文件上传，在请求头中携带token
export async function postFormData(url, formData) {
    const response = await fetch(`${BASE_URL}${url}`, {
        method: 'POST',
        headers: {
            'token': getToken()
        },
        body: formData
    });
    
    return handleResponse(response);
}

// 存储token
export function setToken(token) {
    localStorage.setItem('token', token);
}

// 清除token
export function removeToken() {
    localStorage.removeItem('token');
}

// 格式化日期时间
export function formatDateTime(dateInput) {
    // 处理不同类型的输入
    let date;
    
    if (!dateInput) {
        return '未知时间';
    }
    
    // 如果已经是Date对象
    if (dateInput instanceof Date) {
        date = dateInput;
    }
    // 如果是数字（时间戳），需要判断是秒还是毫秒
    else if (typeof dateInput === 'number') {
        // 如果是秒级时间戳（小于10位数），转换为毫秒
        if (dateInput < 10000000000) {
            date = new Date(dateInput * 1000);
        } else {
            date = new Date(dateInput);
        }
    }
    // 如果是字符串
    else if (typeof dateInput === 'string') {
        // 尝试直接解析
        date = new Date(dateInput);
        // 如果解析失败，可能是秒级时间戳字符串
        if (isNaN(date.getTime()) && !isNaN(Number(dateInput))) {
            const timestamp = Number(dateInput);
            if (timestamp < 10000000000) {
                date = new Date(timestamp * 1000);
            } else {
                date = new Date(timestamp);
            }
        }
    }
    // 其他情况
    else {
        date = new Date(dateInput);
    }
    
    // 检查日期是否有效
    if (isNaN(date.getTime())) {
        return '无效时间';
    }
    
    return date.getFullYear() + '-' + 
           String(date.getMonth() + 1).padStart(2, '0') + '-' + 
           String(date.getDate()).padStart(2, '0') + ' ' + 
           String(date.getHours()).padStart(2, '0') + ':' + 
           String(date.getMinutes()).padStart(2, '0');
}

// 格式化货币
export function formatCurrency(amount) {
    const yuan = parseFloat(amount) || 0;
    return '¥' + yuan.toFixed(2);
}